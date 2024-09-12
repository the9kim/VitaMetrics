package com.vitametrics.vitametrics.sleep.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Sleep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timeAwakened;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timeAsleep;

    public Sleep(LocalDateTime timeAwakened, LocalDateTime timeAsleep) {
        this.timeAwakened = timeAwakened;
        this.timeAsleep = timeAsleep;
    }

    public Long getId() {
        return id;
    }
}
