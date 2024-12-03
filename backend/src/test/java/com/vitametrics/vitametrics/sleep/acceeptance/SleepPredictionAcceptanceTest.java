package com.vitametrics.vitametrics.sleep.acceeptance;

import com.vitametrics.vitametrics.common.AcceptanceTest;
import com.vitametrics.vitametrics.common.fixtures.acceptance.SleepPredictionAcceptanceFixture;
import com.vitametrics.vitametrics.sleep.application.SleepPredictionService;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionResponse;
import com.vitametrics.vitametrics.sleep.presentation.SleepPredictionController;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static com.vitametrics.vitametrics.common.fixtures.acceptance.SleepPredictionAcceptanceFixture.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SleepPredictionAcceptanceTest extends AcceptanceTest {

    @Autowired
    private SleepPredictionController sleepPredictionController;

    @Mock
    private SleepPredictionService sleepPredictionService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(sleepPredictionController, "sleepPredictionService", sleepPredictionService);
    }

    @Test
    void shouldPredictSleepDuration() {
        // given
        String predictionDate = "2024-11-17";
        SleepPredictionRequest request = new SleepPredictionRequest(predictionDate);
        SleepPredictionResponse predictionResponse = new SleepPredictionResponse(7, 50);
        when(sleepPredictionService.getSleepPrediction(request)).thenReturn(new SleepPredictionResponse(7, 50));

        // when
        ExtractableResponse<Response> response = GET_SLEEP_PREDICTION_REQUEST(predictionDate);

        // then
        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                    softly.assertThat(response.jsonPath().getInt("hours")).isEqualTo(predictionResponse.hours());
                    softly.assertThat(response.jsonPath().getInt("minutes")).isEqualTo(predictionResponse.minutes());
                }
        );
    }

    @Test
    void shouldThrowExceptionWhenPredictingSleepDurationWithInvalidDateFormat() {
        // given
        String invalidDate = "2024";

        // when
        ExtractableResponse<Response> response = GET_SLEEP_PREDICTION_REQUEST(invalidDate);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().jsonPath().getString("errors[0].rejectedValue")).isEqualTo(invalidDate);
            softly.assertThat(response.body().jsonPath().getString("errors[0].message")).isEqualTo("Invalid date format. Expected format: yyyy-MM-dd");
        });
    }

    @Test
    void shouldThrowExceptionWhenPredictingSleepDurationWithEmptyDateValue() {
        // given
        String invalidDate = "";

        // when
        ExtractableResponse<Response> response = GET_SLEEP_PREDICTION_REQUEST(invalidDate);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().jsonPath().getString("errors[0].message")).isEqualTo("Date must not be blank");
        });
    }
}
