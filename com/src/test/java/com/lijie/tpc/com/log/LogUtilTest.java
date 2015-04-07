package com.lijie.tpc.com.log;

import com.google.common.annotations.VisibleForTesting;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

/**
 * lijie2pc on 2015/3/22.
 */
public class LogUtilTest {


    public void testWrite(){
        LogUtil.init("abc.txt");
        LogUtil.getInstance().log(123, "fff", null);
        LogUtil.getInstance().log(456, "rrr", null);
    }

    @Test
    public void testRead(){
        LogUtil.init("abc.txt");
        List<Log> logList = LogUtil.getInstance().getLogList();

        Assert.assertEquals(2, logList.size());
        Assert.assertEquals(new Integer(123), logList.get(0).getId());
    }

    @Test
    public void getVoteYesButNoCommitOrAbort(){
        LogUtil.init("bcd.txt");
        List<Log> logList = LogUtil.getInstance().getVoteYesButNoCommitOrAbort();

        org.junit.Assert.assertEquals(1, logList.size());
        org.junit.Assert.assertEquals(Log.VOTE_YES_STEP, logList.get(0).getStep());
    }
}
