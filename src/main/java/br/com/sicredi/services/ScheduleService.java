package br.com.sicredi.services;

import br.com.sicredi.interfaces.json.schedule.OpenSessionRequest;
import br.com.sicredi.interfaces.json.schedule.SchedulePutRequest;

public interface ScheduleService {

    String createNewSchedule(SchedulePutRequest putRequest);

    void openNewSession(Long scheduleId, OpenSessionRequest sessionRequest) throws Exception;
}
