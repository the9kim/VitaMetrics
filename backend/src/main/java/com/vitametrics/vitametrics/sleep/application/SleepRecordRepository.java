package com.vitametrics.vitametrics.sleep.application;

import com.vitametrics.vitametrics.sleep.domain.SleepRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long> {
}
