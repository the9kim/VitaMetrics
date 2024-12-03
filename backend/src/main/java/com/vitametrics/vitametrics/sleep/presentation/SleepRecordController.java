package com.vitametrics.vitametrics.sleep.presentation;

import com.vitametrics.vitametrics.sleep.application.SleepRecordService;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterResponse;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordSearchResponse;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/sleep")
@RequiredArgsConstructor
public class SleepRecordController {

    private final SleepRecordService sleepRecordService;

    @PostMapping
    public ResponseEntity<Void> register(
            @Valid @RequestBody final SleepRecordRegisterRequest request) {
        final SleepRecordRegisterResponse response = sleepRecordService.register(request);
        final URI location = URI.create("/api/sleep/" + response.sleepRecordId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{sleepRecordId}")
    public ResponseEntity<SleepRecordSearchResponse> search(@PathVariable final Long sleepRecordId) {
        final SleepRecordSearchResponse sleepRecord = sleepRecordService.search(sleepRecordId);
        return ResponseEntity.ok(sleepRecord);
    }

    @PatchMapping("/{sleepRecordId}")
    public ResponseEntity<Void> update(
            @PathVariable final Long sleepRecordId,
            @Valid @RequestBody final SleepRecordUpdateRequest request
    ) {
        sleepRecordService.update(request, sleepRecordId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sleepRecordId}")
    public ResponseEntity<Void> delete(@PathVariable final Long sleepRecordId) {
        sleepRecordService.delete(sleepRecordId);
        return ResponseEntity.noContent().build();
    }

}
