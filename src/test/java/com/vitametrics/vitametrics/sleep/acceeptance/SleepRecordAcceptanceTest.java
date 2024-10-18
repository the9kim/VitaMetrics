package com.vitametrics.vitametrics.sleep.acceeptance;

import com.vitametrics.vitametrics.common.AcceptanceTest;
import com.vitametrics.vitametrics.sleep.application.SleepRecordRepository;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordUpdateRequest;
import com.vitametrics.vitametrics.sleep.domain.SleepRecord;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.format.DateTimeFormatter;

import static com.vitametrics.vitametrics.common.fixtures.SleepRecordFixtures.*;
import static com.vitametrics.vitametrics.common.fixtures.acceptance.SleepRecordAcceptanceFixtures.*;

public class SleepRecordAcceptanceTest extends AcceptanceTest {

    private static final String BASE_URL = "/api/sleep";

    @Autowired
    private SleepRecordRepository sleepRecordRepository;

    @Nested
    class Register {
        @Test
        void shouldRegisterSleepRecord() {
            // given
            SleepRecordRegisterRequest registerRequest = SLEEP_REGISTER_REQUEST_FROM_23_TO_7_AVERAGE;

            // when
            final ExtractableResponse<Response> response = SLEEP_RECORD_REGISTER_REQUEST(registerRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(response.header(HttpHeaders.LOCATION)).contains(BASE_URL);
            });
        }

        @Test
        void shouldThrowExceptionWhenRegisteringWithInvalidTemporalSequence() {

            // given
            SleepRecordRegisterRequest invalidRequest = INVALID_SLEEP_REGISTER_REQUEST_FROM_07_TO_23_AVERAGE;
            String expectedMessage = String.format(
                    "Invalid temporal order: bedTime (%s) is after wakeTime (%s)",
                    invalidRequest.bedTime(),
                    invalidRequest.wakeTime());

            // when
            ExtractableResponse<Response> response = SLEEP_RECORD_REGISTER_REQUEST(invalidRequest);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.jsonPath().getString("error")).contains(HttpStatus.BAD_REQUEST.getReasonPhrase());
                softly.assertThat(response.jsonPath().getString("message")).contains(expectedMessage);
            });
        }
    }

    @Nested
    class Search {

        @Test
        void shouldSearchSleepRecord() {

            // given
            SleepRecord savedSleepRecord = testFixtureBuilder.buildSleepRecord(SLEEP_RECORD_FROM_23_TO_07_AVERAGE());

            // when
            final ExtractableResponse<Response> response = SLEEP_RECORD_SEARCH_REQUEST(savedSleepRecord.getId());

            // then
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(response.body().jsonPath().getString("id")).isEqualTo((savedSleepRecord.getId().toString()));
                softly.assertThat(response.body().jsonPath().getString("bedTime")).isEqualTo(savedSleepRecord.getBedTime().format(dateFormat));
                softly.assertThat(response.body().jsonPath().getString("wakeTime")).isEqualTo(savedSleepRecord.getWakeTime().format(dateFormat));
                softly.assertThat(response.body().jsonPath().getString("quality")).isEqualTo(savedSleepRecord.getQuality().toString());
            });
        }

        @Test
        void shouldThrowExceptionWhenSearchingWithNonExistSleepRecordId() {

            // given
            Long nonExistId = Long.MIN_VALUE;
            String expectedErrorMessage = String.format(
                    "sleep record not found - request details {sleep_record_id: %d}", nonExistId);

            // when
            ExtractableResponse<Response> response = SLEEP_RECORD_SEARCH_REQUEST(nonExistId);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                softly.assertThat(response.body().jsonPath().getString("error")).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
                softly.assertThat(response.body().jsonPath().getString("message")).isEqualTo(expectedErrorMessage);
            });
        }
    }

    @Nested
    class Update {

        @Test
        void shouldUpdateSleepRecord() {

            // given
            SleepRecord savedSleepRecord = testFixtureBuilder.buildSleepRecord(SLEEP_RECORD_FROM_23_TO_07_AVERAGE());
            SleepRecordUpdateRequest updateRequest = SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD;

            // when
            ExtractableResponse<Response> response = SLEEP_RECORD_UPDATE_REQUEST(updateRequest, savedSleepRecord.getId());

            // then
            SoftAssertions.assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

                        SleepRecord updatedSleepRecord = sleepRecordRepository.findById(savedSleepRecord.getId()).orElseThrow();

                        softly.assertThat(updatedSleepRecord.getBedTime()).isEqualTo(updateRequest.bedTime());
                        softly.assertThat(updatedSleepRecord.getWakeTime()).isEqualTo(updateRequest.wakeTime());
                        softly.assertThat(updatedSleepRecord.getQuality()).isEqualTo(updateRequest.quality());
                    }
            );
        }

        @Test
        void shouldThrowExceptionWhenUpdatingWithNonExistSleepRecordId() {

            // given
            Long nonExistId = Long.MIN_VALUE;
            SleepRecordUpdateRequest updateRequest = SLEEP_UPDATE_REQUEST_FROM_22_TO_6_BAD;
            String expectedErrorMessage = String.format(
                    "sleep record not found - request details {sleep_record_id: %d}", nonExistId);

            // when
            ExtractableResponse<Response> response = SLEEP_RECORD_UPDATE_REQUEST(updateRequest, nonExistId);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                softly.assertThat(response.body().jsonPath().getString("error")).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
                softly.assertThat(response.body().jsonPath().getString("message")).isEqualTo(expectedErrorMessage);
            });
        }

        @Test
        void shouldThrowExceptionWhenUpdatingWithInvalidTemporalOrder() {

            // given
            SleepRecord savedSleepRecord = testFixtureBuilder.buildSleepRecord(SLEEP_RECORD_FROM_23_TO_07_AVERAGE());
            SleepRecordUpdateRequest invalidUpdateRequest = INVALID_SLEEP_Update_REQUEST_FROM_07_TO_23_AVERAGE;
            String expectedErrorMessage = String.format("Invalid temporal order: bedTime (%s) is after wakeTime (%s)", invalidUpdateRequest.bedTime(), invalidUpdateRequest.wakeTime());

            // when
            ExtractableResponse<Response> response = SLEEP_RECORD_UPDATE_REQUEST(invalidUpdateRequest, savedSleepRecord.getId());

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                softly.assertThat(response.body().jsonPath().getString("error")).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase());
                softly.assertThat(response.body().jsonPath().getString("message")).isEqualTo(expectedErrorMessage);
            });
        }

    }

    @Nested
    class Delete {

        @Test
        void shouldDeleteSleepRecord() {

            // given
            SleepRecord savedSleepRecord = testFixtureBuilder.buildSleepRecord(SLEEP_RECORD_FROM_23_TO_07_AVERAGE());

            // when
            ExtractableResponse<Response> response = SLEEP_RECORD_DELETE_REQUEST(savedSleepRecord.getId());

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                softly.assertThat(sleepRecordRepository.existsById(savedSleepRecord.getId())).isFalse();
            });
        }

        @Test
        void shouldThrowExceptionWhenDeletingWithNonExistId() {

            // given
            Long nonExistId = Long.MIN_VALUE;
            String expectedErrorMessage = String.format(
                    "sleep record not found - request details {sleep_record_id: %d}", nonExistId);

            // when
            ExtractableResponse<Response> response = SLEEP_RECORD_DELETE_REQUEST(nonExistId);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
                softly.assertThat(response.body().jsonPath().getString("error")).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
                softly.assertThat(response.body().jsonPath().getString("message")).isEqualTo(expectedErrorMessage);
            } );
        }
    }
}
