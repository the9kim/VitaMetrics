package com.vitametrics.vitametrics.sleep.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SleepPredictionRequest(

        @Pattern(
                regexp = "\\d{4}-\\d{2}-\\d{2}",
                message = "Invalid date format. Expected format: yyyy-MM-dd"

        )
        @NotBlank(message = "Date must not be blank")
        String date
) {
}
