package br.com.sicredi.kafka;

import br.com.sicredi.interfaces.json.kafka.PautaResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@Slf4j
public class KafkaProducerImpl implements KafkaProducer {

    private final KafkaTemplate<String, PautaResult> kafkaTemplate;
    private final String topicName;

    public KafkaProducerImpl(
            final KafkaTemplate<String, PautaResult> kafkaTemplate,
            @Value("${kafka.topic.vote-result}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    @Async
    public void sendPautaResults(PautaResult pautaResult) {
        try{
            log.info("Enviando mensagem para o topico");
            this.kafkaTemplate.send(topicName, pautaResult);
        }catch (Exception ex) {
            log.warn(MessageFormat.format("Erro ao enviar a mensagem para o topico=[{0}] erro=[{1}]",
                     topicName, ExceptionUtils.getRootCause(ex)));
        }
    }
}
