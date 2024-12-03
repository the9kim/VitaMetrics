package com.vitametrics.vitametrics.learning;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitametrics.vitametrics.sleep.application.SleepPredictionService;
import com.vitametrics.vitametrics.sleep.application.SleepRecordService;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionResponse;
import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

public class MockLearningTest {

    @Autowired
    SleepPredictionService realService;


    @Test
    void shouldReturnStubbedValue() {
        // Arrange
        SleepPredictionService mockService = mock(SleepPredictionService.class);
        SleepPredictionRequest request = new SleepPredictionRequest("2024-11-17");
        int expectedHours = 7;
        int expectedMinutes = 50;
        SleepPredictionResponse expectedResponse = new SleepPredictionResponse(expectedHours, expectedMinutes);
        when(mockService.getSleepPrediction(request)).thenReturn(expectedResponse);

        // Act
        SleepPredictionResponse response = mockService.getSleepPrediction(new SleepPredictionRequest("2024-11-17"));

        // Assert
        Assertions.assertThat(response.hours()).isEqualTo(expectedHours);
        Assertions.assertThat(response.minutes()).isEqualTo(expectedMinutes);

    }

    @Test
    void shouldVerifyMethodCall() {
        // Arrange
        SleepPredictionService mockService = mock(SleepPredictionService.class);
        SleepPredictionRequest request = new SleepPredictionRequest("2024-11-17");

        // Act
        mockService.getSleepPrediction(request);

        // Assert

        /**
         * 	•	Mockito ensures that:
         * 	•	The method processData was called.
         * 	•	The argument passed to the method was ""2024-11-17"".
         */
        verify(mockService).getSleepPrediction(new SleepPredictionRequest("2024-11-17"));
    }

    @Test
    void shouldStubWithArgumentMatcher() {
        // Arrange
        SleepPredictionService mockService = mock(SleepPredictionService.class);
        int expectedHours = 7;
        int expectedMinutes = 50;
        SleepPredictionResponse expectedResponse = new SleepPredictionResponse(expectedHours, expectedMinutes);

        /**
         * 	•	anyString():
         * 	    •	Matches any String argument passed to the method.
         * 	    •	Allows flexibility in tests where the exact argument value doesn’t matter.
         * 	•	anyInt(): Matches any int value.
         * 	•	any(): Matches any object of any type.
         * 	•	eq("value"): Matches the exact value "value".
         */
        when(mockService.getSleepPrediction(any())).thenReturn(expectedResponse);

        // Act
        mockService.getSleepPrediction(new SleepPredictionRequest("2024-11-11"));

        // Assert
        Assertions.assertThat(expectedResponse.hours()).isEqualTo(expectedHours);
        Assertions.assertThat(expectedResponse.minutes()).isEqualTo(expectedMinutes);
    }

    @Test
    void shouldVerifyNumberOfInteractions() {
        // Arrange
        SleepPredictionService mockService = mock(SleepPredictionService.class);
        SleepPredictionRequest request = new SleepPredictionRequest("2024-11-17");

        // Act
        mockService.getSleepPrediction(request);
        mockService.getSleepPrediction(request);

        // Assert
        verify(mockService, times(2)).getSleepPrediction(new SleepPredictionRequest("2024-11-17"));
    }

    @Test
    void shouldThrowExceptionOnStubbedCall() {
        // Arrange
        SleepPredictionService mockService = mock(SleepPredictionService.class);
        String invalidDate = "2024-13-10";
        SleepPredictionRequest invalidRequest = new SleepPredictionRequest(invalidDate);
        when(mockService.getSleepPrediction(invalidRequest)).thenThrow(new SleepRecordException.InvalidDateFormatException(invalidDate));

        // Act & Assert
        Assertions.assertThatThrownBy(() -> mockService.getSleepPrediction(invalidRequest))
                .isInstanceOf(SleepRecordException.InvalidDateFormatException.class);
    }

    @Test
    void shouldStubUsingDoReturn() {
        // Arrange
        SleepPredictionService mockService = mock(SleepPredictionService.class);
        SleepPredictionRequest request = new SleepPredictionRequest("2024-11-17");

        int expectedHours = 7;
        int expectedMinutes = 50;
        SleepPredictionResponse expectedResponse = new SleepPredictionResponse(expectedHours, expectedMinutes);

        /**
         *  Why Use doReturn Instead of when?
         *
         *  1. Final Methods
         *      •	If the method being stubbed is final, when(...).thenReturn(...) will fail with a MockitoException.
         *
         *  2. Void Methods
         *      •	For methods with a void return type, you can use doReturn alongside doNothing() or doThrow().
         */
        doReturn(expectedResponse).when(mockService).getSleepPrediction(request);

        // Act
        SleepPredictionResponse response = mockService.getSleepPrediction(request);

        // Assert
        Assertions.assertThat(response.hours()).isEqualTo(expectedHours);
        Assertions.assertThat(response.minutes()).isEqualTo(expectedMinutes);
    }

    @Test
    void shouldVerifyNoInteractions() {
        // Arrange
        SleepPredictionService mockService = mock(SleepPredictionService.class);

        // Assert
        verifyNoInteractions(mockService);
    }

    @Test
    void shouldCaptureArgument() throws JsonProcessingException {
        // Arrange
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        RestTemplate mockRestTemplate = mock(RestTemplate.class);

        SleepPredictionService sleepPredictionService = new SleepPredictionService(mockRestTemplate, mockObjectMapper);
        ReflectionTestUtils.setField(sleepPredictionService, "djangoApiUrl", "http://localhost:8000");

        SleepPredictionRequest request = new SleepPredictionRequest("2024-11-17");
        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);

        when(mockObjectMapper.writeValueAsString(request)).thenReturn("{\"date\": \"2024-11-17\"}");
        when(mockRestTemplate.postForEntity(
                eq("http://localhost:8000/api/sleep/predict"),
                captor.capture(),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok("{\"hours\": 7, \"minutes\": 50}"));


        // Act
        SleepPredictionResponse response = sleepPredictionService.getSleepPrediction(request);

        // Verify
        Assertions.assertThat(response.hours()).isEqualTo(7);
        Assertions.assertThat(response.minutes()).isEqualTo(50);
    }

    @Test
    void shouldUsePartialMockWithSpy() {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        RestTemplate spyRestTemplate = spy(restTemplate);

        SleepPredictionService sleepPredictionService = new SleepPredictionService(spyRestTemplate, objectMapper);
        ReflectionTestUtils.setField(sleepPredictionService, "djangoApiUrl", "http://localhost:8000");

        /**
         *  •	spy(realService):
         * 	    •	Wraps the real MyService object in a spy.
         * 	    •	A spy behaves like the real object but allows selective stubbing of its methods.
         * 	    •	Non-stubbed methods retain their original implementation.
         */

        SleepPredictionRequest request = new SleepPredictionRequest("2024-11-17");
        int expectedHours = 7;
        int expectedMinutes = 50;
        SleepPredictionResponse expectedResponse = new SleepPredictionResponse(expectedHours, expectedMinutes);

        /**
         * 	•	doReturn(...).when(...).methodCall(...):
         * 	    •	Stubs the getData("input") method on the spy to return "mocked value".
         * 	    •	Unlike when(...).thenReturn(...), the doReturn(...).when(...) syntax is more appropriate for spies because it avoids invoking the actual method during stubbing.
         */

        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        doReturn(ResponseEntity.ok("{\"hours\": 7, \"minutes\": 50}")).when(spyRestTemplate).postForEntity(
                eq("http://localhost:8000/api/sleep/predict"),
                captor.capture(),
                eq(String.class));

        // Act
        SleepPredictionResponse response = sleepPredictionService.getSleepPrediction(request);

        // Assert
        Assertions.assertThat(response.hours()).isEqualTo(expectedHours);
        Assertions.assertThat(response.minutes()).isEqualTo(expectedMinutes);
    }

    @Test
    void shouldStubbingVoidMethod() {
        // Arrange
        SleepRecordService mockService = mock(SleepRecordService.class);
        Long sleepRecordId = 1L;

        /**
         * 	•	doNothing():
         * 	    •	Specifies that the void method (deleteData) should do nothing when called.
         * 	    •	This is the default behavior for void methods in Mockito, but explicitly stubbing it can improve clarity.
         */
        doNothing().when(mockService).delete(sleepRecordId);

        // Act
        mockService.delete(sleepRecordId);

        // Assert
        verify(mockService).delete(1L);
    }
}
