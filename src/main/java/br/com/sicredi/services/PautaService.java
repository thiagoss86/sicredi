package br.com.sicredi.services;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;

import java.util.List;

public interface PautaService {

    String createNewPauta(PautaRequest putRequest);

    void openNewSession(Long scheduleId, OpenSessionRequest sessionRequest);

    Pauta getPauta(Long scheduleId);

    List<Pauta> getAllOpenPautas();

    void updatePautaAndSendMessage(Pauta pauta);
}
