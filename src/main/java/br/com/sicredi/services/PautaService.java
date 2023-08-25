package br.com.sicredi.services;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;

public interface PautaService {

    String createNewPauta(PautaRequest putRequest);

    void openNewSession(Long scheduleId, OpenSessionRequest sessionRequest) throws Exception;

    Pauta getPauta(Long scheduleId) throws Exception;
}
