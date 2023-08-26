package br.com.sicredi.builders.pauta;

import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;

import java.time.LocalDateTime;

public class OpenSessionTestBuilder {

    private OpenSessionTestBuilder(){}

    public static OpenSessionRequest getOpenSessionRequest(LocalDateTime limitTime) {
        return OpenSessionRequest.builder()
                .limitTime(limitTime)
                .build();
    }
}
