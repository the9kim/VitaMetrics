package com.vitametrics.vitametrics.sleep.application;

import com.vitametrics.vitametrics.sleep.application.dto.SleepRegisterRequest;
import com.vitametrics.vitametrics.sleep.domain.Sleep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SleepService {
    private final SleepRepository sleepRepository;

    public SleepService(SleepRepository sleepRepository) {
        this.sleepRepository = sleepRepository;
    }

    public Long registerTime(final SleepRegisterRequest request) {

        Sleep sleep = sleepRepository.save(new Sleep(request.timeAwakened(), request.timeAsleep()));

        return sleep.getId();
    }
}
