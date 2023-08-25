package br.com.sicredi.exeptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //BAD NOT FOUND
    PAUTA_NOT_FOUND("404.001"),

    //REGRAS DE NEGOCIO
    PAUTA_ALREADY_OPEN("422.001"),
    PAUTA_ALREADY_CLOSED("422.002"),
    ASSOCIATED_ALREADY_VOTE("422.003");

    private final String code;
}
