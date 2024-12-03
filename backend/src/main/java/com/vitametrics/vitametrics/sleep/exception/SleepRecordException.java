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

    public static class WrongSleepDurationFormatException extends SleepRecordException {
        public WrongSleepDurationFormatException(String response) {
            super(String.format(
                    "Fail to parse response: " + response
            ));
        }
    }

    public static class InvalidTimeFormatException extends SleepRecordException {
        public InvalidTimeFormatException(int hours, int minutes) {
            super(String.format(
                    "Invalid time format: (%d)hour (%d)minutes", hours, minutes)
            );
        }
    }

    public static class InvalidDateFormatException extends SleepRecordException {
        public InvalidDateFormatException(String date) {
            super(String.format(
                    "Invalid date format: %s, ", date
            ));
        }
    }

}
