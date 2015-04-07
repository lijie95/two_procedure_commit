package com.lijie.tpc.par.record;

import com.lijie.tpc.com.json.AbstractJsonFile;

import java.io.File;

/**
 * lijie2pc on 2015/3/22.
 */
public class RecordManager extends AbstractJsonFile<Record> {
    public RecordManager(File jsonFile) {
        super(jsonFile);
    }

    public void record(Record record){
        addAndWrite(record);
    }
}
