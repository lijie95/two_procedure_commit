package com.lijie.tpc.par;

import com.lijie.tpc.com.socket.IMessageHandler;
import com.lijie.tpc.com.socket.ObjectClient;
import com.lijie.tpc.com.socket.message.IMessage;
import com.lijie.tpc.com.socket.message.TPCMessage;
import com.lijie.tpc.com.util.JobQueueProcessor;

/**
 * lijie2pc on 2015/3/22.
 */
public class ParticipantMessageHandler implements IMessageHandler {

    private JobQueueProcessor queueProcessor;

    private JobProcessor jobProcessor;

    public ParticipantMessageHandler(JobQueueProcessor<CoordinatorRequest> coordinatorRequestJobQueueProcessor, JobProcessor jobProcessor) {
        queueProcessor = coordinatorRequestJobQueueProcessor;
        this.jobProcessor = jobProcessor;
    }

    @Override
    public void handleMessage(IMessage IMessage, ObjectClient objectClient) {

        TPCMessage tpcMessage = (TPCMessage) IMessage;

        if(tpcMessage.getOperation()==TPCMessage.ADD_OPERATION
                || tpcMessage.getOperation()==TPCMessage.SUB_OPERATION
                || tpcMessage.getOperation()==TPCMessage.RECORD_OPERATION){
            queueProcessor.put(new CoordinatorRequest(tpcMessage, objectClient));
        }else{
            jobProcessor.setResponse(tpcMessage);
        }



    }

}
