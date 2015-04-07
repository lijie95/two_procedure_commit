package com.lijie.tpc.com.socket.message;

import com.lijie.tpc.com.socket.message.IMessage;
import com.lijie.tpc.com.socket.message.UserMessage;
import com.lijie.tpc.com.util.ServerInfo;

import java.util.List;

/**
 * lijie2pc on 2015/3/21.
 */
public class TPCMessage implements IMessage {

    public static final int RECORD_OPERATION = 0;
    public static final int SUB_OPERATION = 1;
    public static final int ADD_OPERATION = 2;

    public static final int ABORT_OPERATION = 3;
    public static final int COMMIT_OPERATION = 4;


    private int operation = 0; //0 no meaning; 1 sub, 2 addAndWrite;

    private UserMessage userMessage;

    private List<ServerInfo> serverList;

    private String response;
    private int transactionId;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<ServerInfo> getServerList() {
        return serverList;
    }

    public void setServerList(List<ServerInfo> serverList) {
        this.serverList = serverList;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
