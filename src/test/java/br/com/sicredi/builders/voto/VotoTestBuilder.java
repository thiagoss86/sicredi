package br.com.sicredi.builders.voto;

import br.com.sicredi.domain.voto.VotoValue;
import br.com.sicredi.interfaces.json.voto.VotoRequest;

public class VotoTestBuilder {

    private VotoTestBuilder(){}

    public static VotoRequest getVotoRequest(String cpf, VotoValue votoValue) {
        return VotoRequest.builder()
                .cpf(cpf)
                .votoValue(votoValue)
                .build();
    }
}
