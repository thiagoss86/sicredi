package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;
import br.com.sicredi.services.PautaService;
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
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<Void> postSchedule(
            @RequestBody @Valid PautaRequest putRequest) {

        var newScheduleId = pautaService.createNewPauta(putRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("id", newScheduleId).build();
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> openSession(
            @PathVariable Long scheduleId,
            @RequestBody @Valid OpenSessionRequest sessionRequest) throws Exception {

        pautaService.openNewSession(scheduleId, sessionRequest);

        return ResponseEntity.ok("Sessão aberta com sucesso!");
    }
}
