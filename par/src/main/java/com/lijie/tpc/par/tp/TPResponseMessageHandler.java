package com.lijie.tpc.par.tp;

import com.lijie.tpc.com.log.Log;
import com.lijie.tpc.com.log.LogUtil;
import com.lijie.tpc.com.socket.IMessageHandler;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.IMessage;
import com.lijie.tpc.com.socket.message.TPCMessage;
import com.lijie.tpc.par.JobProcessor;
import com.lijie.tpc.par.Participant;

import java.util.List;

/**
 * lijie2pc on 2015/3/22.
 */
public class TPResponseMessageHandler implements IMessageHandler {
    @Override
    public void handleMessage(IMessage IMessage, ObjectClient objectClient) {
        TPMessage tpMessage = (TPMessage) IMessage;

        //check if the transaction is still in uncertain status.
        //because this transaction may be commit or abort by other tp server.
        List<Log> logList = LogUtil.getInstance().getVoteYesButNoCommitOrAbort();
        Log uncertainLog = null;
        for(Log log : logList){
            if(log.getId() == tpMessage.getTransactionId()){
                uncertainLog = log;
                break;
            }
        }

        if(uncertainLog != null){
            if(tpMessage.getResult()==TPMessage.ABORT_RESULT){
                LogUtil.getInstance().addAndWrite(new Log(tpMessage.getTransactionId(), Log.RECEIVE_ABORT_STEP, null));
            }else if(tpMessage.getResult()==TPMessage.COMMIT_RESULT){

                TPCMessage tpcMessage = uncertainLog.getMemo();

                String accountPathName = "log/" + Participant.SERVER_NAME + "/" + tpcMessage.getUserMessage().getUserName();
                String recordPathName = String.format("log/%s/record.txt", Participant.SERVER_NAME);
                JobProcessor.processCommit(tpcMessage, tpcMessage.getUserMessage(), accountPathName, recordPathName);
            }
        }

        objectClient.stop();
    }
}
