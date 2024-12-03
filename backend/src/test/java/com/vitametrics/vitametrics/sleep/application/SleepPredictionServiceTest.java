package com.vitametrics.vitametrics.sleep.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitametrics.vitametrics.common.ServiceTest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionResponse;
import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

class SleepPredictionServiceTest extends ServiceTest {

    private SleepPredictionService sleepPredictionService;

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        sleepPredictionService = new SleepPredictionService(restTemplate, objectMapper);
        ReflectionTestUtils.setField(sleepPredictionService, "djangoApiUrl", "http://localhost:8000");
    }

    @Test
    void shouldGetSleepPrediction() throws JsonProcessingException {
        // given
        String date = "2024-11-17";
        SleepPredictionRequest request = new SleepPredictionRequest(date);
        SleepPredictionResponse expectedResponse = new SleepPredictionResponse(7, 50);

        String mockJsonResponse = "{\"hours\": 7, \"minutes\": 50}";
        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);

        when(restTemplate.postForEntity(
                eq("http://localhost:8000/api/sleep/predict"),
                captor.capture(),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok(mockJsonResponse));

        // when
        SleepPredictionResponse response = sleepPredictionService.getSleepPrediction(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(response).isNotNull();
                    softly.assertThat(response.hours()).isEqualTo(expectedResponse.hours());
                    softly.assertThat(response.minutes()).isEqualTo(expectedResponse.minutes());

                }
        );

        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void shouldThrowExceptionWhenGettingSleepPredictionWithInvalidResponse() {

        // given
        String date = "2024-11-17";
        SleepPredictionRequest request = new SleepPredictionRequest(date);

        String invalidResponseBody = "notDateFormat";
        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);

        when(restTemplate.postForEntity(
                eq("http://localhost:8000/api/sleep/predict"),
                captor.capture(),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok(invalidResponseBody));

        // when & then
        Assertions.assertThatThrownBy(() -> sleepPredictionService.getSleepPrediction(request))
                .isInstanceOf(SleepRecordException.WrongSleepDurationFormatException.class)
                .hasMessage("Fail to parse response: " + invalidResponseBody);
    }
}
