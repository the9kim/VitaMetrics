package com.vitametrics.vitametrics.sleep.domain;

import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SleepRecordTest {

    @Test
    void shouldCreateSleepingRecordObject() {
        // given
        LocalDateTime bedTime = LocalDateTime.of(2024, 1, 1, 23, 0);
        LocalDateTime wakeTime = LocalDateTime.of(2024, 1, 2, 7, 0);
        SleepingQuality quality = SleepingQuality.AVERAGE;

        // when & then
        assertThatCode(() -> new SleepRecord(bedTime, wakeTime, quality))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("The wake-up time is earlier than the bedtime")
    void calculateSleepingTime_WithInvalidTemporalSequence_ThrowsException() {
        // given
        LocalDateTime bedTime = LocalDateTime.of(2024, 12, 31, 23, 0);
        LocalDateTime wakeTime = LocalDateTime.of(2024, 1, 1, 7, 0);
        SleepingQuality quality = SleepingQuality.AVERAGE;

        // when & then
        assertThatThrownBy(() ->
                new SleepRecord(bedTime, wakeTime, quality))
                .isInstanceOf(SleepRecordException.class)
                .hasMessage(String.format("Invalid temporal order: bedTime (%s) is after wakeTime (%s)", bedTime, wakeTime));
    }
}
