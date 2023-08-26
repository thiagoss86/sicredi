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
@RequestMapping("/pautas")
@RequiredArgsConstructor
@Valid
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<Void> postPauta(
            @RequestBody @Valid PautaRequest pautaRequest) {

        var newPauta = pautaService.createNewPauta(pautaRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("id", String.valueOf(newPauta.getId())).build();
    }

    @PutMapping("/{pautaId}")
    public ResponseEntity<String> openSession(
            @PathVariable Long pautaId,
            @RequestBody @Valid OpenSessionRequest sessionRequest) {

        pautaService.openNewSession(pautaId, sessionRequest);

        return ResponseEntity.ok("Sessão aberta com sucesso!");
    }
}
