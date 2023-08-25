package br.com.sicredi.services;

import br.com.sicredi.interfaces.json.SchedulePutRequest;

public interface ScheduleService {

    String createNewSchedule(SchedulePutRequest putRequest);
}
