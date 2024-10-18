package com.vitametrics.vitametrics.sleep.exception;

import java.time.LocalDateTime;

public class SleepRecordException extends RuntimeException {

    public SleepRecordException(String message) {
        super(message);
    }


    public static class TemporalOrderException extends SleepRecordException {
        public TemporalOrderException(LocalDateTime bedTime, LocalDateTime wakeTime) {
            super(String.format("Invalid temporal order: bedTime (%s) is after wakeTime (%s)", bedTime, wakeTime));
        }
    }

    public static class NonExistSleepRecordException extends SleepRecordException {
        public NonExistSleepRecordException(Long sleepRecordId) {
            super(String.format(
                    "sleep record not found - request details {sleep_record_id: %d}", sleepRecordId)
            );
        }
    }

}
