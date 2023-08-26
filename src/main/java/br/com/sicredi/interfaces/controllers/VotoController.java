package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.interfaces.json.voto.VotoRequest;
import br.com.sicredi.services.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votos")
@RequiredArgsConstructor
@Valid
@Tag(name = "Voto Endpoint", description = "Endpoint para abertura de sessão")
public class VotoController {

    private final VotoService votoService;

    @Operation(
            summary = "Abrir Sessão",
            description = "Endpoint para abrir sessão para uma pauta habilitada",
            tags = {
                    "Sessão"
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Campo(s) da requisição invalido(s)"),
                    @ApiResponse(responseCode = "404", description = "Pauta informada não localizada"),
                    @ApiResponse(responseCode = "422", description = "Erro de regra de negocio"),
                    @ApiResponse(responseCode = "500", description = "Erro interno de sistema")
            }
    )
    @PostMapping(value = "/{pautaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerVote(
            @PathVariable Long pautaId,
            @RequestBody @Valid VotoRequest votoRequest) {

        votoService.registerVoto(pautaId, votoRequest);

        return ResponseEntity.ok("Voto registado com sucesso!");
    }
}
