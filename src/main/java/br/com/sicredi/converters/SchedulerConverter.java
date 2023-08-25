package br.com.sicredi.converters;

import br.com.sicredi.domain.schedule.Schedule;
import br.com.sicredi.domain.schedule.ScheduleSessionStatus;
import br.com.sicredi.interfaces.json.SchedulePutRequest;

public class SchedulerConverter {

    private SchedulerConverter() {}

    public static Schedule toDomain(SchedulePutRequest putRequest) {
        return Schedule.builder()
                .name(putRequest.getName())
                .sessionStatus(ScheduleSessionStatus.CLOSED)
                .build();
    }
}
