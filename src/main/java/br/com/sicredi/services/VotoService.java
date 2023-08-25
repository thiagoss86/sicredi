package br.com.sicredi.services;

import br.com.sicredi.interfaces.json.voto.VotoRequest;

public interface VotoService {

    void registerVoto(Long scheduleId, VotoRequest votoRequest);
}
