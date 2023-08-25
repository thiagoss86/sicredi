package br.com.sicredi.services;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;

import java.util.List;

public interface PautaService {

    String createNewPauta(PautaRequest putRequest);

    void openNewSession(Long scheduleId, OpenSessionRequest sessionRequest) throws Exception;

    Pauta getPauta(Long scheduleId) throws Exception;

    List<Pauta> getAllOpenPautas();

    void updatePauta(Pauta pauta);
}
