package com.lijie.tpc.coo;

import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.UserMessage;

/**
 * lijie2pc on 2015/3/21.
 */
public class UserRequest {
    private UserMessage userMessage;

    private ObjectClient objectClient;

    public ObjectClient getObjectClient() {
        return objectClient;
    }

    public void setObjectClient(ObjectClient objectClient) {
        this.objectClient = objectClient;
    }

    public UserMessage getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }

    public UserRequest(UserMessage message, ObjectClient objectClient) {
        this.userMessage = message;
        this.objectClient = objectClient;
    }
}
