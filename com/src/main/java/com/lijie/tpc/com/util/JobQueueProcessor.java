package com.lijie.tpc.com.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * lijie2pc on 2015/3/21.
 */
public class JobQueueProcessor<T> implements Runnable {

    private BlockingQueue<T> jobQueue = new LinkedBlockingQueue<T>();

    private IJobProcessor jobProcessor;

    public JobQueueProcessor(IJobProcessor jobProcessor) {
        this.jobProcessor = jobProcessor;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()){
            try {
                T userRequest = jobQueue.take();
                this.jobProcessor.processRequest(userRequest);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void put(T userRequest) {
        try {
            jobQueue.put(userRequest);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
