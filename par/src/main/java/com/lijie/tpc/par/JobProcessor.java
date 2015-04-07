package com.lijie.tpc.par;

import com.lijie.tpc.com.log.Log;
import com.lijie.tpc.com.log.LogUtil;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.TPCMessage;
import com.lijie.tpc.com.socket.message.UserMessage;
import com.lijie.tpc.com.util.IJobProcessor;
import com.lijie.tpc.com.util.ServerInfo;
import com.lijie.tpc.par.account.Account;
import com.lijie.tpc.par.account.AccountManager;
import com.lijie.tpc.par.record.Record;
import com.lijie.tpc.par.record.RecordManager;
import com.lijie.tpc.par.tp.TPMessage;
import com.lijie.tpc.par.tp.TPResponseMessageHandler;
import com.lijie.tpc.par.tp.TerminateProtocol;

import java.io.File;
import java.io.IOException;

/**
 * lijie2pc on 2015/3/22.
 */
public class JobProcessor implements IJobProcessor<CoordinatorRequest> {

    private TPCMessage responseMessage;

    private int transactionId;

    @Override
    public void processRequest(CoordinatorRequest coordinatorRequest) {
        TPCMessage tpcMessage = coordinatorRequest.getTpcMessage();
        transactionId = tpcMessage.getTransactionId();

        UserMessage userMessage = tpcMessage.getUserMessage();
        String accountPathName = "log/" + Participant.SERVER_NAME + "/" + userMessage.getUserName();
        String recordPathName = String.format("log/%s/record.txt", Participant.SERVER_NAME);

        switch (tpcMessage.getOperation()){
            case TPCMessage.SUB_OPERATION:
                File accountFile = new File(accountPathName);
                AccountManager accountManager = new AccountManager(accountFile);
                Account account = accountManager.getAccount();
                if(account.getAmount() < userMessage.getAmmount()){
                    sendAbort(tpcMessage,coordinatorRequest.getObjectClient());
                    clearTransaction();
                    return;//vote no, so end this thread;
                }else{
                    sendOK(tpcMessage,coordinatorRequest.getObjectClient());
                }
                break;
            case TPCMessage.ADD_OPERATION:
            case TPCMessage.RECORD_OPERATION:
                sendOK(tpcMessage,coordinatorRequest.getObjectClient());
                break;
        }

        try {
            Thread.sleep(Participant.WAITING_RESPONSE_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(responseMessage != null){
            if(responseMessage.getOperation()==TPCMessage.COMMIT_OPERATION){
                processCommit(tpcMessage, userMessage, accountPathName, recordPathName);
            }else{
                LogUtil.getInstance().addAndWrite(new Log(tpcMessage.getTransactionId(), Log.RECEIVE_ABORT_STEP, null));
            }
        }else{
            LogUtil.getInstance().addAndWrite(new Log(tpcMessage.getTransactionId(), Log.START_TP_STEP, null));
            TerminateProtocol.proccessTP(tpcMessage);
        }

        clearTransaction();
    }

    public static void processCommit(TPCMessage tpcMessage, UserMessage userMessage, String accountPathName, String recordPathName) {
        LogUtil.getInstance().addAndWrite(new Log(tpcMessage.getTransactionId(), Log.RECEIVE_COMMIT_STEP, null));
        File accountFile = new File(accountPathName);
        AccountManager accountManager = new AccountManager(accountFile);
        Account account = accountManager.getAccount();
        switch (tpcMessage.getOperation()){
            case TPCMessage.SUB_OPERATION:
                account.setAmount(account.getAmount() - userMessage.getAmmount());
                accountManager.setAccount(account);
                break;
            case TPCMessage.ADD_OPERATION:
                account.setAmount(account.getAmount() + userMessage.getAmmount());
                accountManager.setAccount(account);
                break;
            case TPCMessage.RECORD_OPERATION:
                File recordFile = new File(recordPathName);
                RecordManager recordManager = new RecordManager(recordFile);
                recordManager.record(new Record(userMessage.getUserName(), userMessage.getAmmount(), userMessage.getSourceBank(), userMessage.getDestBank()));
                break;
        }
    }

    private void clearTransaction() {
        responseMessage=null;
        transactionId=-1;
    }


    private void sendOK(TPCMessage tpcMessage, ObjectClient objectClient) {
        LogUtil.getInstance().addAndWrite(new Log(tpcMessage.getTransactionId(), Log.VOTE_YES_STEP, tpcMessage));
        tpcMessage.setResponse(Boolean.TRUE.toString());
        try {
            objectClient.sendMessage(tpcMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendAbort(TPCMessage tpcMessage, ObjectClient objectClient) {
        LogUtil.getInstance().addAndWrite(new Log(tpcMessage.getTransactionId(), Log.VOTE_NO_STEP, null));
        tpcMessage.setResponse(Boolean.FALSE.toString());
        try {
            objectClient.sendMessage(tpcMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setResponse(TPCMessage tpcMessage) {
        if(transactionId == tpcMessage.getTransactionId()){
            this.responseMessage = tpcMessage;
        }
    }

    public boolean onTransaction() {
        return transactionId!=-1;
    }
}
