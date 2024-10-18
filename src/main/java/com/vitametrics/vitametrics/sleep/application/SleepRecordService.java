package com.vitametrics.vitametrics.sleep.application;

import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterRequest;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordRegisterResponse;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordSearchResponse;
import com.vitametrics.vitametrics.sleep.application.dto.SleepRecordUpdateRequest;
import com.vitametrics.vitametrics.sleep.domain.SleepRecord;
import com.vitametrics.vitametrics.sleep.exception.SleepRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SleepRecordService {

    private final SleepRecordRepository sleepRecordRepository;

    public SleepRecordRegisterResponse register(final SleepRecordRegisterRequest request) {

        final SleepRecord savedSleepRecord = sleepRecordRepository.save(createSleepRecord(request));

        return new SleepRecordRegisterResponse(savedSleepRecord.getId());
    }

    public SleepRecordSearchResponse search(final Long sleepRecordId) {

        final SleepRecord savedSleepRecord = findOrFail(sleepRecordId);

        return new SleepRecordSearchResponse(savedSleepRecord);
    }

    public void update(final SleepRecordUpdateRequest request, final Long sleepRecordId) {
        final SleepRecord savedSleepRecord = findOrFail(sleepRecordId);

        savedSleepRecord.changeRecord(request.bedTime(), request.wakeTime(), request.quality());
    }

    public void delete(final Long sleepRecordId) {
        final SleepRecord savedSleepRecord = findOrFail(sleepRecordId);
        sleepRecordRepository.delete(savedSleepRecord);
    }

    private SleepRecord createSleepRecord(SleepRecordRegisterRequest request) {
        return new SleepRecord(request.bedTime(), request.wakeTime(), request.quality());
    }

    private SleepRecord findOrFail(final Long sleepRecordId) {
        return sleepRecordRepository.findById(sleepRecordId)
                .orElseThrow(() -> new SleepRecordException.NonExistSleepRecordException(sleepRecordId));
    }

}
