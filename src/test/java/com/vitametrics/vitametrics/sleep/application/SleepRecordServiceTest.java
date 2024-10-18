package com.vitametrics.vitametrics.sleep.application;

import com.vitametrics.vitametrics.common.ServiceTest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterResponse;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordSearchResponse;
import com.vitametrics.vitametrics.sleep.domain.SleepRecord;
import com.vitametrics.vitametrics.sleep.domain.SleepingQuality;
import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.vitametrics.vitametrics.common.fixtures.SleepRecordFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SleepRecordServiceTest extends ServiceTest {

    @Autowired
    private SleepRecordService sleepRecordService;

    @Autowired
    private SleepRecordRepository sleepRecordRepository;


    @Nested
    class Register {
        @Test
        void shouldRegisterSleepRecord() {
            // when
            SleepRecordRegisterResponse response = sleepRecordService.register(SLEEP_REGISTER_REQUEST_FROM_23_TO_7_AVERAGE);
            SleepRecord savedSleepRecord = sleepRecordRepository.findById(response.sleepRecordId()).orElseThrow();

            // then
            assertSavedSleepRecord(
                    savedSleepRecord,
                    SLEEP_REGISTER_REQUEST_FROM_23_TO_7_AVERAGE.bedTime(),
                    SLEEP_REGISTER_REQUEST_FROM_23_TO_7_AVERAGE.wakeTime(),
                    SLEEP_REGISTER_REQUEST_FROM_23_TO_7_AVERAGE.quality());
        }

        @Test
        void shouldThrowExceptionWhenRegisteringWithInvalidTemporalSequence() {
            // given
            SleepRecordRegisterRequest invalidRequest = INVALID_SLEEP_REGISTER_REQUEST_FROM_07_TO_23_AVERAGE;
            String expectedMessage = String.format("Invalid temporal order: bedTime (%s) is after wakeTime (%s)",
                    INVALID_SLEEP_REGISTER_REQUEST_FROM_07_TO_23_AVERAGE.bedTime(),
                    INVALID_SLEEP_REGISTER_REQUEST_FROM_07_TO_23_AVERAGE.wakeTime());

            // when & then
            assertThatThrownBy(() -> sleepRecordService.register(invalidRequest))
                    .isInstanceOf(SleepRecordException.TemporalOrderException.class)
                    .hasMessage(expectedMessage);
        }
    }


    @Nested
    class Search {

        @Test
        void shouldSearchSleepRecord() {
            // given
            SleepRecord savedSleepRecord = testFixtureBuilder.buildSleepRecord(SLEEP_RECORD_FROM_23_TO_07_AVERAGE());
            SleepRecordSearchResponse expected = new SleepRecordSearchResponse(savedSleepRecord);

            // when
            SleepRecordSearchResponse response = sleepRecordService.search(savedSleepRecord.getId());

            // then
            assertThat(response).isEqualTo(expected);
        }

        @Test
        void shouldThrowExceptionWhenSearchingSleepRecordWithNonExistSleepRecordId() {
            // given
            long nonExistId = Long.MIN_VALUE;
            String expectedMessage = String.format(
                    "sleep record not found - request details {sleep_record_id: %d}", nonExistId);

            // when && then
            assertThatThrownBy(() -> sleepRecordService.search(nonExistId))
                    .isInstanceOf(SleepRecordException.NonExistSleepRecordException.class)
                    .hasMessage(expectedMessage);
        }
    }


    @Nested
    class Update {
        @Test
        void shouldModifySleepRecord() {
            // given
            SleepRecord savedSleepRecord = testFixtureBuilder.buildSleepRecord(SLEEP_RECORD_FROM_23_TO_07_AVERAGE());

            // when
            sleepRecordService.update(SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD, savedSleepRecord.getId());
            SleepRecord updatedRecord = sleepRecordRepository.findById(savedSleepRecord.getId()).orElseThrow();

            // then
            assertSavedSleepRecord(
                    updatedRecord,
                    SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD.bedTime(),
                    SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD.wakeTime(),
                    SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD.quality()
            );

        }

        @Test
        void shouldThrowExceptionWhenUpdatingSleepRecordWithNonExistSleepRecordId() {
            // given
            long nonExistId = Long.MIN_VALUE;
            String expectedMessage = String.format(
                    "sleep record not found - request details {sleep_record_id: %d}", nonExistId);

            // when && then
            assertThatThrownBy(() -> sleepRecordService.update(SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD, nonExistId))
                    .isInstanceOf(SleepRecordException.NonExistSleepRecordException.class)
                    .hasMessage(expectedMessage);
        }
    }


    @Nested
    class Delete {
        @Test
        void shouldDeleteSleepRecord() {
            // given
            Long savedRecordId = testFixtureBuilder.buildSleepRecord(SLEEP_RECORD_FROM_23_TO_07_AVERAGE()).getId();

            // when
            sleepRecordService.delete(savedRecordId);

            // then
            assertThat(sleepRecordRepository.existsById(savedRecordId)).isFalse();
        }

        @Test
        void shouldThrowExceptionWhenDeletingSleepRecordWithNonExistSleepRecordId() {
            // given
            long nonExistId = Long.MIN_VALUE;
            String expectedMessage = String.format(
                    "sleep record not found - request details {sleep_record_id: %d}", nonExistId);

            // when && then
            assertThatThrownBy(() -> sleepRecordService.delete(nonExistId))
                    .isInstanceOf(SleepRecordException.NonExistSleepRecordException.class)
                    .hasMessage(expectedMessage);
        }
    }

    private void assertSavedSleepRecord(SleepRecord savedSleepRecord, LocalDateTime betTIme, LocalDateTime wakeTime, SleepingQuality quality) {
        assertSoftly(softly ->
                {
                    softly.assertThat(savedSleepRecord.getBedTime()).isEqualTo(betTIme);
                    softly.assertThat(savedSleepRecord.getWakeTime()).isEqualTo(wakeTime);
                    softly.assertThat(savedSleepRecord.getQuality()).isEqualTo(quality);
                }
        );
    }

}
