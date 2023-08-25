package br.com.sicredi.kafka;

import br.com.sicredi.interfaces.json.kafka.PautaResult;

public interface KafkaProducer {

    void sendPautaResults(PautaResult pautaResult);
}
