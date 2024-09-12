package com.vitametrics.vitametrics.sleep.presentation;

import com.vitametrics.vitametrics.sleep.application.SleepService;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/sleep")
public class SleepController {

    private final SleepService sleepService;

    public SleepController(SleepService sleepService) {
        this.sleepService = sleepService;
    }

    @PostMapping
    public ResponseEntity<Void> register(
            @RequestBody final SleepRegisterRequest request
    ) {
        final Long id = sleepService.registerTime(request);
        final URI location = URI.create("/api/sleep/" + id);
        return ResponseEntity.created(location).build();
    }

}
