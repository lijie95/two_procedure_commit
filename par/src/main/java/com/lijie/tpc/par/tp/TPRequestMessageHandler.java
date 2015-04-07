package com.lijie.tpc.par.tp;

import com.lijie.tpc.com.log.LogUtil;
import com.lijie.tpc.com.socket.IMessageHandler;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.IMessage;
import com.lijie.tpc.com.socket.message.TPCMessage;
import com.lijie.tpc.par.JobProcessor;

import java.io.IOException;

/**
 * lijie2pc on 2015/3/22.
 */
public class TPRequestMessageHandler implements IMessageHandler {

    private JobProcessor jobProcessor;

    public TPRequestMessageHandler(JobProcessor jobProcessor) {
        this.jobProcessor = jobProcessor;
    }

    @Override
    public void handleMessage(IMessage IMessage, ObjectClient objectClient) {
        TPMessage tpMessage = (TPMessage) IMessage;
        if(LogUtil.getInstance().isTransactionCommit(tpMessage.getTransactionId())){
            tpMessage.setResult(TPMessage.COMMIT_RESULT);
        }else if(LogUtil.getInstance().isTransactionAbort(tpMessage.getTransactionId())){
            tpMessage.setResult(TPMessage.ABORT_RESULT);
        }else {
            //wait the transaction stop
            //1. now I am processing the previous transaction. after finish, I send unknown to TP partner.
            //2. now I am processing current transaction. after finish, I send result to TP partner.
            waitCurrentTransactionFinish();
            if(LogUtil.getInstance().isTransactionCommit(tpMessage.getTransactionId())){
                tpMessage.setResult(TPMessage.COMMIT_RESULT);
            }else if(LogUtil.getInstance().isTransactionAbort(tpMessage.getTransactionId())){
                tpMessage.setResult(TPMessage.ABORT_RESULT);
            }else{
                tpMessage.setResult(TPMessage.UNKNOWN_RESULT);
            }
        }
        tpMessage.setOperation(TPMessage.RESPONSE_OPT);

        try {
            objectClient.sendMessage(tpMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitCurrentTransactionFinish() {
        while (!Thread.currentThread().isInterrupted() && jobProcessor.onTransaction()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
