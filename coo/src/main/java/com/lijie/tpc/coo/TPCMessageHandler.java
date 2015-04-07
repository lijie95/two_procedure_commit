package com.lijie.tpc.coo;

import com.lijie.tpc.com.socket.IMessageHandler;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.IMessage;
import com.lijie.tpc.com.socket.message.TPCMessage;

/**
 * lijie2pc on 2015/3/21.
 */
public class TPCMessageHandler implements IMessageHandler {

    private JobProcessor jobProcessor;

    private String serverName;

    public TPCMessageHandler(JobProcessor jobProcessor, String serverName) {

        this.serverName = serverName;
        this.jobProcessor = jobProcessor;
    }

    @Override
    public void handleMessage(IMessage message, ObjectClient objectClient) {

        TPCMessage tpcMessage = (TPCMessage) message;

        jobProcessor.setVoteResponse(tpcMessage.getTransactionId(), serverName, Boolean.valueOf(tpcMessage.getResponse()));
    }
}
