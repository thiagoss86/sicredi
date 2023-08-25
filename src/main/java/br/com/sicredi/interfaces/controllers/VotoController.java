package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.interfaces.json.voto.VotoRequest;
import br.com.sicredi.services.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Valid
public class VotoController {

    private final VotoService votoService;

    @PostMapping("/{scheduleId}")
    public ResponseEntity<String> registerVote(
            @PathVariable Long scheduleId,
            @RequestBody @Valid VotoRequest putRequest) throws Exception {

        votoService.registerVoto(scheduleId, putRequest);

        return ResponseEntity.ok("Voto registado com sucesso");
    }
}
