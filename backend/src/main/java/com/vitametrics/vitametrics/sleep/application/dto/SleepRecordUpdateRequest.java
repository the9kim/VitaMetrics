package com.vitametrics.vitametrics.sleep.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vitametrics.vitametrics.sleep.domain.SleepingQuality;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SleepRecordUpdateRequest(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @NotNull(message = "Bed time must not be null")
        LocalDateTime bedTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        @NotNull(message = "Wake time must not be null")
        LocalDateTime wakeTime,

        @NotNull(message = "Sleeping quality must not be null")
        SleepingQuality quality
) {
}
