package br.com.sicredi.converters;

import br.com.sicredi.domain.schedule.Schedule;
import br.com.sicredi.domain.schedule.ScheduleStatus;
import br.com.sicredi.interfaces.json.SchedulePutRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class SchedulerConverter {

    public static Schedule toDomain(SchedulePutRequest putRequest) {
        var scheduleDomain = Schedule.builder()
                .name(putRequest.getName())
                .status(ScheduleStatus.ACTIVE)
                .build();

        Optional.ofNullable(putRequest.getLimitTime())
                .ifPresentOrElse(scheduleDomain::setLimitTime,
                        () -> scheduleDomain.setLimitTime(LocalDateTime.now().plus(1, ChronoUnit.MINUTES))
                        );

        return scheduleDomain;
    }
}
