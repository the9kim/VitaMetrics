package com.vitametrics.vitametrics.sleep.exception;

import java.time.LocalDateTime;

public class SleepRecordException extends RuntimeException {

    public SleepRecordException(String message) {
        super(message);
    }


    public static class TemporalOrderException extends SleepRecordException {
        public TemporalOrderException(String bedTime, String wakeTime) {
            super(String.format("기상 시간은 취침 시간보다 빠를 수 없습니다. - request info {bed_time: %s, wake_time: %s", bedTime, wakeTime));
        }
    }

}
