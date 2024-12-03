package com.vitametrics.vitametrics.sleep.presentation;

import com.vitametrics.vitametrics.sleep.application.SleepPredictionService;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepPredictionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sleep")
@RequiredArgsConstructor
public class SleepPredictionController {

    private final SleepPredictionService sleepPredictionService;

    @GetMapping(value = "/predict-sleep", params = "date")
    public ResponseEntity<SleepPredictionResponse> getPrediction(@Valid SleepPredictionRequest request) {
        SleepPredictionResponse sleepPrediction = sleepPredictionService.getSleepPrediction(request);
        return ResponseEntity.ok(sleepPrediction);
    }

}
