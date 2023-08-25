package br.com.sicredi.converters;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.voto.Voto;
import br.com.sicredi.interfaces.json.voto.VotoRequest;

public class VotoConverter {

    private VotoConverter(){}

    public static Voto toDomain(VotoRequest votoRequest, Pauta pauta) {
        return Voto.builder()
                .associateCpf(votoRequest.getCpf())
                .votoValue(votoRequest.getVotoValue())
                .pauta(pauta)
                .build();
    }
}
