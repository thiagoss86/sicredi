package br.com.sicredi.converters;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;

public class PautaConverter {

    private PautaConverter() {}

    public static Pauta toDomain(PautaRequest putRequest) {
        return Pauta.builder()
                .name(putRequest.getName())
                .sessionStatus(PautaSessionStatus.CLOSED)
                .build();
    }

}
