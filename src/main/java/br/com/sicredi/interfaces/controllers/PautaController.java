package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;
import br.com.sicredi.services.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@Tag(name = "Pauta Endpoint", description = "Endpoint para cadastro de pauta e abertura de sessão")
public class PautaController {

    private final PautaService pautaService;

    @Operation(
            summary = "Cadastra nova Pauta",
            description = "Realiza o cadastro de uma nova pauta",
            tags = {
                    "Pauta"
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cadastro realiado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Campo(s) da requisição invalido(s)"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no sistema")
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postPauta(
            @RequestBody @Valid PautaRequest pautaRequest) {

        var newPauta = pautaService.createNewPauta(pautaRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("id", String.valueOf(newPauta.getId())).build();
    }

    @Operation(
            summary = "Abre nova Sessão",
            description = "Realiza a abertura de uma nova sessão para uma pauta",
            tags = {
                    "Pauta",
                    "Sessão"
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sessão aberta com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Campos da requisição invalidos"),
                    @ApiResponse(responseCode = "404", description = "Pauta não localizada para abertura de sessão"),
                    @ApiResponse(responseCode = "422", description = "Regra de negocio violada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do sistema")
            }
    )
    @PutMapping(value = "/{pautaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> openSession(
            @PathVariable Long pautaId,
            @RequestBody @Valid OpenSessionRequest sessionRequest) {

        pautaService.openNewSession(pautaId, sessionRequest);

        return ResponseEntity.ok("Sessão aberta com sucesso!");
    }
}
