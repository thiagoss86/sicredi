package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.interfaces.json.schedule.OpenSessionRequest;
import br.com.sicredi.interfaces.json.schedule.SchedulePutRequest;
import br.com.sicredi.services.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedulers")
@RequiredArgsConstructor
@Valid
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> postSchedule(
            @RequestBody @Valid SchedulePutRequest putRequest) {

        var newScheduleId = scheduleService.createNewSchedule(putRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("id", newScheduleId).build();
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> openSession(
            @PathVariable Long scheduleId,
            @RequestBody @Valid OpenSessionRequest sessionRequest) throws Exception {

        scheduleService.openNewSession(scheduleId, sessionRequest);

        return ResponseEntity.ok("Sessão aberta com sucesso!");
    }
}
