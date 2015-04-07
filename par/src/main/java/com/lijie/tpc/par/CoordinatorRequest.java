package com.lijie.tpc.par;

import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.TPCMessage;

/**
 * lijie2pc on 2015/3/22.
 */
public class CoordinatorRequest {

    private TPCMessage tpcMessage;

    public CoordinatorRequest(TPCMessage tpcMessage, ObjectClient objectClient) {
        this.tpcMessage = tpcMessage;
        this.objectClient = objectClient;
    }

    public ObjectClient getObjectClient() {

        return objectClient;
    }

    public TPCMessage getTpcMessage() {
        return tpcMessage;
    }

    private ObjectClient objectClient;


}
