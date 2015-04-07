package com.lijie.tpc.com.log;

import com.google.common.collect.Lists;
import com.lijie.tpc.com.json.AbstractJsonFile;
import com.lijie.tpc.com.socket.message.TPCMessage;

import java.io.File;
import java.util.List;

/**
 * lijie2pc on 2015/3/22.
 */
public class LogUtil extends AbstractJsonFile<Log> {

    private static LogUtil instance = null;

    private LogUtil(File file) {
        super(file);
    }

    synchronized public void  log(Integer id, String step, TPCMessage memo){
        addAndWrite(new Log(id, step, memo));
    }

    public List<Log> getLogList(){
        return getObjectList();
    }

    public static void init(String fileName){
        File dir = new File("log");
        File newFile = new File(dir, fileName);
        if(instance == null){
            instance = new LogUtil(newFile);
        }
    }

    public static LogUtil getInstance(){
        return instance;
    }

    public int getAvailableTransactionId() {
        return getObjectList().size()==0 ? 0 : getObjectList().get(getObjectList().size()-1).getId()+1;
    }

    public boolean isTransactionCommit(int transactionId) {
        List<Log> logList = getLogList();
        for(Log log: logList){
            if(log.getId() == transactionId && log.getStep().equals(Log.RECEIVE_COMMIT_STEP)){
                return true;
            }
        }
        return false;
    }

    public boolean isTransactionAbort(int transactionId) {
        List<Log> logList = getLogList();
        for(Log log: logList){
            if(log.getId() == transactionId && log.getStep().equals(Log.RECEIVE_ABORT_STEP)){
                return true;
            }
        }
        return false;
    }

    public Log getVoteYesByTransactionId(int transactionId) {
        List<Log> logList = getLogList();
        for(Log log: logList){
            if(log.getId() == transactionId && log.getStep().equals(Log.VOTE_YES_STEP)){
                return log;
            }
        }
        return null;
    }

    public List<Log> getVoteYesButNoCommitOrAbort() {
        List<Log> logList = getLogList();
        List<Log> result = Lists.newArrayList();
        int transactionId = -1;
        boolean vote = false;
        boolean uncertain = true;
        Log lastVotLog = null;
        for(Log log : logList){
            if(transactionId != log.getId()){
                if(uncertain && lastVotLog != null){
                    result.add(lastVotLog);
                }
                transactionId = log.getId();
            }
            if(log.getStep().equals(Log.VOTE_YES_STEP)){
                vote = true;
                lastVotLog = log;
            }
            if(vote && (log.getStep().equals(Log.RECEIVE_ABORT_STEP)
                    || log.getStep().equals(Log.RECEIVE_COMMIT_STEP))){
                uncertain = false;
            }
        }

        if(uncertain && lastVotLog != null){
            result.add(lastVotLog);
        }

        return result;
    }
}
