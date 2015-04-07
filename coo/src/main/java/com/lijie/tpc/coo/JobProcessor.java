package com.lijie.tpc.coo;

import com.lijie.tpc.com.log.Log;
import com.lijie.tpc.com.log.LogUtil;
import com.lijie.tpc.com.socket.message.TPCMessage;
import com.lijie.tpc.com.socket.message.UserMessage;
import com.lijie.tpc.com.util.IJobProcessor;
import com.lijie.tpc.com.util.SocketConst;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * lijie2pc on 2015/3/21.
 */
public class JobProcessor implements IJobProcessor<UserRequest> {

    private Coordinator coordinator;

    private int transactionId;

    private ConcurrentHashMap<String, Boolean> voteMap = new ConcurrentHashMap<String, Boolean>();

    public void setVoteResponse(int transactionId, String serverName, Boolean result){

        if(transactionId == this.transactionId){//do not process other transaction

            voteMap.put(serverName, result);
        }
    }

    public JobProcessor(Coordinator coordinator) {

        this.coordinator = coordinator;
    }

    @Override
    public void processRequest(UserRequest userRequest){

        transactionId = LogUtil.getInstance().getAvailableTransactionId();

        LogUtil.getInstance().addAndWrite(new Log(transactionId, Log.VOTE_REQUEST_STEP, null));
        sendTPCVoteRequest(userRequest, transactionId);
        //if the request failed after Coordinator.WAITING_RESPONSE_TIME
        //there will not be validate result, and the operation will be abort there.

        try {
            Thread.sleep(Coordinator.WAITING_RESPONSE_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(voteMap.size() < SocketConst.getServerInfoList().size() ){
            LogUtil.getInstance().addAndWrite(new Log(transactionId, Log.WAIT_VOTE_RESPONSE_TIME_OUT_STEP, null));
            sendTPCAbort();
            String response = "timeout to wait vote response from bank";
            responseUser(userRequest, response);
            clearTransaction();
            return;
        }
        if(voteMap.containsValue(Boolean.FALSE)){
            LogUtil.getInstance().addAndWrite(new Log(transactionId, Log.ABORT_STEP, null));
            sendTPCAbort();
            StringBuilder stringBuilder = new StringBuilder();
            for(Map.Entry<String, Boolean> entry : voteMap.entrySet()){
                if(entry.getValue() == Boolean.FALSE){
                    stringBuilder.append(entry.getKey());
                    stringBuilder.append(",");
                }
            }
            String response = "bank (%s) request to abort your request";
            responseUser(userRequest, String.format(response, stringBuilder.toString()));
            clearTransaction();
            return;
        }

        LogUtil.getInstance().addAndWrite(new Log(transactionId, Log.COMMIT_STEP, null));
        sendTPCCommit();
        //if send commit failed, the participant should follow the "termination protocol".

        responseUser(userRequest, "success");
        clearTransaction();
    }

    private void clearTransaction() {
        transactionId=-1;
        voteMap.clear();
    }

    private void sendTPCCommit() {
        TPCMessage tpcMessage = new TPCMessage();
        tpcMessage.setOperation(TPCMessage.COMMIT_OPERATION);
        tpcMessage.setTransactionId(transactionId);
        for(Map.Entry<String, Boolean> entry : voteMap.entrySet()){
            try {
                //test code
//                if(entry.getKey().equals(SocketConst.BOC_SERVER_NAME)){
//                    continue;
//                }
                    coordinator.getServerMap().get(entry.getKey()).sendMessage(tpcMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void responseUser(UserRequest userRequest, String response) {
        UserMessage userResponse = new UserMessage();

        userResponse.setResponse(response);
        try {
            userRequest.getObjectClient().sendMessage(userResponse);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void sendTPCAbort() {
        TPCMessage tpcMessage = new TPCMessage();
        tpcMessage.setOperation(TPCMessage.ABORT_OPERATION);
        tpcMessage.setTransactionId(transactionId);
        for(Map.Entry<String, Boolean> entry : voteMap.entrySet()){
            try {
                if(entry.getValue()){
                    coordinator.getServerMap().get(entry.getKey()).sendMessage(tpcMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean sendTPCVoteRequest(UserRequest userRequest, int transactionId) {
        UserMessage userMessage = userRequest.getUserMessage();
        TPCMessage tpcSubMessage = getTpcMessage(userMessage, TPCMessage.SUB_OPERATION, transactionId);
        TPCMessage tpcAddMessage = getTpcMessage(userMessage, TPCMessage.ADD_OPERATION, transactionId);
        TPCMessage tpcLogMessage = getTpcMessage(userMessage, TPCMessage.RECORD_OPERATION, transactionId);

        try {
            coordinator.getServerMap().get(userMessage.getSourceBank()).sendMessage(tpcSubMessage);
            coordinator.getServerMap().get(userMessage.getDestBank()).sendMessage(tpcAddMessage);
            coordinator.getServerMap().get(SocketConst.CBRC_SERVER_NAME).sendMessage(tpcLogMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private TPCMessage getTpcMessage(UserMessage userMessage, int operation, int transactionId) {
        TPCMessage tpcSubMessage = new TPCMessage();

        tpcSubMessage.setTransactionId(transactionId);
        tpcSubMessage.setOperation(operation);
        tpcSubMessage.setUserMessage(userMessage);
        tpcSubMessage.setServerList(SocketConst.getServerInfoList());
        return tpcSubMessage;
    }
}
