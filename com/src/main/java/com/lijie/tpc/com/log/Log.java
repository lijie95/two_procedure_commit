package com.lijie.tpc.com.log;

import com.lijie.tpc.com.socket.message.TPCMessage;

/**
 * lijie2pc on 2015/3/22.
 */
public class Log {

    private Integer id;

    private String step;

    public TPCMessage getMemo() {
        return memo;
    }

    public void setMemo(TPCMessage memo) {
        this.memo = memo;
    }

    private TPCMessage memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Log(Integer id, String step, TPCMessage memo) {
        this.id = id;
        this.step = step;
        this.memo = memo;
    }

    public static final String VOTE_REQUEST_STEP = "VOTE_REQUEST";
    public static final String WAIT_VOTE_RESPONSE_TIME_OUT_STEP = "WAIT_VOTE_RESPONSE_TIME_OUT_STEP";
    public static final String COMMIT_STEP = "COMMIT_STEP";
    public static final String ABORT_STEP = "ABORT_STEP";

    public static final String VOTE_YES_STEP = "VOTE_YES_STEP";
    public static final String VOTE_NO_STEP = "VOTE_NO_STEP";
    public static final String RECEIVE_COMMIT_STEP = "RECEIVE_COMMIT_STEP";
    public static final String RECEIVE_ABORT_STEP = "RECEIVE_ABORT_STEP";
    public static final String START_TP_STEP = "START_TP_STEP";

    public Log(){}
}
