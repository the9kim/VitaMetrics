package com.vitametrics.vitametrics.sleep.application.dto;

import com.vitametrics.vitametrics.sleep.domain.SleepRecord;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record SleepRecordSearchResponse(
        Long id,
        String sleepingTime,
        String bedTime,
        String wakeTime,
        String quality

) {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SleepRecordSearchResponse(SleepRecord sleepRecord) {
        this(
                sleepRecord.getId(),
                formatDuration(sleepRecord.getSleepingTime()),
                formatDateTime(sleepRecord.getBedTime()),
                formatDateTime(sleepRecord.getWakeTime()),
                sleepRecord.getQuality().name()
        );
    }

    private static String formatDuration(Duration duration) {
        return String.format("%02d:%02d", duration.toHours(), duration.toMinutesPart());
    }

    private static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

}
