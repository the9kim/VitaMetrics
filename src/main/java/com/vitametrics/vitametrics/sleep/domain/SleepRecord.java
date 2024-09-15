package com.vitametrics.vitametrics.sleep.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class SleepRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private double sleepingTime;

    @Column(nullable = false, updatable = false)
    private SleepingQuality quality;

    @Column(nullable = false, updatable = false)
    private LocalDateTime bedTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime wakeTime;

}
