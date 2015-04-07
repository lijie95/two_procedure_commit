package com.lijie.tpc.par.tp;

import com.lijie.tpc.com.socket.message.IMessage;

/**
 * lijie2pc on 2015/3/22.
 */
public class TPMessage implements IMessage {

    public static final int REQUEST_OPT = 0;
    public static final int RESPONSE_OPT = 1;

    public static final int COMMIT_RESULT = 0;
    public static final int ABORT_RESULT = 1;
    public static final int UNKNOWN_RESULT = 2;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    private int transactionId;

    private int operation;

    private int result;

}
