package com.vitametrics.vitametrics.common.fixtures;

import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordUpdateRequest;
import com.vitametrics.vitametrics.sleep.domain.SleepRecord;
import com.vitametrics.vitametrics.sleep.domain.SleepingQuality;

import java.time.LocalDateTime;

public class SleepRecordFixtures {

    public static final LocalDateTime BED_TIME_22 = LocalDateTime.of(2024, 1, 1, 22, 0);
    public static final LocalDateTime BED_TIME_23 = LocalDateTime.of(2024, 1, 1, 23, 0);

    public static final LocalDateTime WAKE_TIME_06 = LocalDateTime.of(2024, 1, 2, 6, 0);
    public static final LocalDateTime WAKE_TIME_07 = LocalDateTime.of(2024, 1, 2, 7, 0);


    public static final SleepRecordRegisterRequest SLEEP_REGISTER_REQUEST_FROM_23_TO_7_AVERAGE = new SleepRecordRegisterRequest(
            BED_TIME_23,
            WAKE_TIME_07,
            SleepingQuality.AVERAGE
    );

    public static final SleepRecordUpdateRequest SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD = new SleepRecordUpdateRequest(
            BED_TIME_22,
            WAKE_TIME_06,
            SleepingQuality.BAD
    );

    public static final SleepRecordRegisterRequest INVALID_SLEEP_REGISTER_REQUEST_FROM_07_TO_23_AVERAGE = new SleepRecordRegisterRequest(
            WAKE_TIME_07,
            BED_TIME_23,
            SleepingQuality.AVERAGE
    );

    public static final SleepRecordUpdateRequest INVALID_SLEEP_Update_REQUEST_FROM_07_TO_23_AVERAGE = new SleepRecordUpdateRequest(
            WAKE_TIME_07,
            BED_TIME_23,
            SleepingQuality.AVERAGE
    );

    public static SleepRecord SLEEP_RECORD_FROM_22_TO_06_BAD() {
        return new SleepRecord(BED_TIME_22, WAKE_TIME_06, SleepingQuality.BAD);
    }

    public static SleepRecord SLEEP_RECORD_FROM_23_TO_07_AVERAGE() {
        return new SleepRecord(BED_TIME_23, WAKE_TIME_07, SleepingQuality.AVERAGE);
    }

}
