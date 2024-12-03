package com.vitametrics.vitametrics.sleep.application.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;

public record SleepPredictionResponse(
        int hours,
        int minutes
) {

    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 23;
    public static final int MIN_MINUTES = 0;
    public static final int MAX_MINUTES = 59;

    public static SleepPredictionResponse parse(String response) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            SleepPredictionResponse parsedResponse = mapper.readValue(response, SleepPredictionResponse.class);
            validate(parsedResponse);
            return parsedResponse;
        } catch (JsonProcessingException e) {
                throw new SleepRecordException.WrongSleepDurationFormatException(response);
        }
    }

    private static void validate(SleepPredictionResponse response) {
        if (response.hours() < MIN_HOUR || response.hours() > MAX_HOUR) {
            throw new SleepRecordException.InvalidTimeFormatException(response.hours(), response.minutes());
        }
        if (response.minutes() < MIN_MINUTES || response.minutes() > MAX_MINUTES) {
            throw new SleepRecordException.InvalidTimeFormatException(response.hours(), response.minutes());
        }
    }
}
