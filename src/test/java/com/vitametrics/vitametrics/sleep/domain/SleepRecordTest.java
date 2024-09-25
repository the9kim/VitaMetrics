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
                .hasMessage("기상 시간은 취침 시간보다 빠를 수 없습니다. - request info {bed_time: 2024-12-31T23:00, wake_time: 2024-01-01T07:00");
    }
}
