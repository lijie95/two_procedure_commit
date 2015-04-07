package com.lijie.tpc.coo;

import com.lijie.tpc.com.socket.IMessageHandler;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.IMessage;
import com.lijie.tpc.com.socket.message.UserMessage;
import com.lijie.tpc.com.util.JobQueueProcessor;

/**
 * lijie2pc on 2015/3/21.
 */
public class UserMessageHandler implements IMessageHandler {

    private JobQueueProcessor jobQueueProcessor;

    public UserMessageHandler(JobQueueProcessor jobQueueProcessor) {
        this.jobQueueProcessor = jobQueueProcessor;
    }

    @Override
    public void handleMessage(IMessage IMessage, ObjectClient objectClient) {
        UserRequest userRequest = new UserRequest((UserMessage) IMessage, objectClient);
        jobQueueProcessor.put(userRequest);
    }
}
