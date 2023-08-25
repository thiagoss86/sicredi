package br.com.sicredi.services.impl;

import br.com.sicredi.converters.SchedulerConverter;
import br.com.sicredi.interfaces.json.SchedulePutRequest;
import br.com.sicredi.repositories.ScheduleRepository;
import br.com.sicredi.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public String createNewSchedule(SchedulePutRequest putRequest) {
        log.info("Creating a new schedule with properties: {}", putRequest.toString());

        var newSchedule = SchedulerConverter.toDomain(putRequest);

        return String.valueOf(scheduleRepository.save(newSchedule).getId());
    }
}
