package com.vitametrics.vitametrics.sleep.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionResponse;
import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class SleepPredictionService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${django.api.url}")
    String djangoApiUrl;

    public SleepPredictionResponse getSleepPrediction(final SleepPredictionRequest request) {
        final String url = UriComponentsBuilder.fromHttpUrl(djangoApiUrl)
                .path("/api/sleep/predict")
                .toUriString();

        HttpEntity<String> entity = buildRequestEntity(request);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return SleepPredictionResponse.parse(response.getBody());

    }

    private HttpEntity<String> buildRequestEntity(final SleepPredictionRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonBody = objectMapper.writeValueAsString(request);

            return new HttpEntity<>(jsonBody, headers);
        } catch (JsonProcessingException e) {
            throw new SleepRecordException.InvalidDateFormatException(request.date());
        }
    }

}
