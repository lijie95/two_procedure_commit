package com.lijie.tpc.com.util;

/**
 * lijie2pc on 2015/3/22.
 */
public interface IJobProcessor<T> {
    void processRequest(T userRequest);
}
