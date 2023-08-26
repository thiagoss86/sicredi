package br.com.sicredi.builders.pauta;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import br.com.sicredi.domain.voto.Voto;
import br.com.sicredi.domain.voto.VotoValue;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;

import java.util.List;

public class PautaTestBuilder {

    private PautaTestBuilder(){}

    public static PautaRequest getPautaRequest(String name) {
        return PautaRequest.builder()
                .name(name)
                .build();
    }

    public static Pauta getPautaDomain(String name) {
        return Pauta.builder()
                .id(1L)
                .name(name)
                .sessionStatus(PautaSessionStatus.CLOSED)
                .build();
    }

    public static Pauta getPautaDomainWithOpenSession(String name) {
        return Pauta.builder()
                .id(1L)
                .name(name)
                .sessionStatus(PautaSessionStatus.OPEN)
                .build();
    }

    public static Pauta getPautaWithVotes(String name) {
        return Pauta.builder()
                .id(1L)
                .name(name)
                .sessionStatus(PautaSessionStatus.OPEN)
                .votos(List.of(
                        Voto.builder()
                                .votoValue(VotoValue.SIM)
                                .build()
                )).build();
    }
}
