package br.com.sicredi.services.impl;

import br.com.sicredi.converters.SchedulerConverter;
import br.com.sicredi.domain.schedule.Schedule;
import br.com.sicredi.domain.schedule.ScheduleSessionStatus;
import br.com.sicredi.interfaces.json.schedule.OpenSessionRequest;
import br.com.sicredi.interfaces.json.schedule.SchedulePutRequest;
import br.com.sicredi.repositories.ScheduleRepository;
import br.com.sicredi.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Override
    @Transactional
    public void openNewSession(Long scheduleId, OpenSessionRequest sessionRequest) throws Exception {
        log.info("Opening a new session for schedule with id:{}", scheduleId);

        var schedule = getScheduleById(scheduleId);

        validAndOpenScheduleSession(schedule);

        Optional.ofNullable(sessionRequest.getLimitTime())
                .ifPresentOrElse(schedule::setLimitTime,
                        () -> schedule.setLimitTime(LocalDateTime.now().plusMinutes(1)));

        scheduleRepository.save(schedule);
        log.info("Session is open with time limit of:{}", schedule.getLimitTime());
    }

    private Schedule getScheduleById(Long scheduleId) throws Exception {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new Exception("Schedule not found"));
    }

    private void validAndOpenScheduleSession(Schedule schedule) throws Exception {
        log.info("Validating if schedule is already opened");

        if (schedule.getSessionStatus().equals(ScheduleSessionStatus.OPEN)) {
            log.error("Session for schedule with id:{} is already open", schedule.getId());
            throw new Exception("Sessão já está aberta!");
        }

        schedule.setSessionStatus(ScheduleSessionStatus.OPEN);
    }
}
