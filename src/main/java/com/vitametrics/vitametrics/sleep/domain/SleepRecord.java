package com.vitametrics.vitametrics.sleep.domain;

import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class SleepRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Duration sleepingTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime bedTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime wakeTime;

    @Enumerated(EnumType.STRING)
    private SleepingQuality quality;

    public SleepRecord(final LocalDateTime bedTime, final LocalDateTime wakeTime, final SleepingQuality quality) {
        validateTemporalOrder(bedTime, wakeTime);
        this.bedTime = bedTime;
        this.quality = quality;
        this.sleepingTime = calculateSleepingTime(bedTime, wakeTime);
    }

    private Duration calculateSleepingTime(final LocalDateTime bedTime, final LocalDateTime wakeTime) {
        return Duration.between(bedTime, wakeTime);
    }

    private void validateTemporalOrder(final LocalDateTime bedTime, final LocalDateTime wakeTime) {
        if (bedTime.isAfter(wakeTime)) {
            throw new SleepRecordException.TemporalOrderException(bedTime.toString(), wakeTime.toString());
        }
    }

}