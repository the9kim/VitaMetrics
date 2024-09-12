package com.vitametrics.vitametrics.sleep.application;

import com.vitametrics.vitametrics.sleep.domain.Sleep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
}
