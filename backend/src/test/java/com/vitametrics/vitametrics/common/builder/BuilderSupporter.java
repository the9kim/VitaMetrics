package com.vitametrics.vitametrics.common.builder;

import com.vitametrics.vitametrics.sleep.application.SleepRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderSupporter {

    @Autowired
    private SleepRecordRepository sleepRecordRepository;

    public SleepRecordRepository sleepRecordRepository() {
        return this.sleepRecordRepository;
    }
}
