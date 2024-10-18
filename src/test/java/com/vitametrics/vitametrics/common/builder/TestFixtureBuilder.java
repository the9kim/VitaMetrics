package com.vitametrics.vitametrics.common.builder;

import com.vitametrics.vitametrics.sleep.domain.SleepRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestFixtureBuilder {

    @Autowired
    private BuilderSupporter bs;


    public SleepRecord buildSleepRecord(SleepRecord sleepRecord) {
        return bs.sleepRecordRepository().save(sleepRecord);
    }


}
