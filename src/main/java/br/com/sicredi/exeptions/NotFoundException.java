package br.com.sicredi.exeptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {

    private final ErrorCode errorCode;
}
