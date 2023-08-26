package br.com.sicredi.kafka;

import br.com.sicredi.interfaces.json.kafka.PautaResult;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerImplTest {

    @InjectMocks
    private KafkaProducerImpl kafkaProducer;

    @Mock
    private KafkaTemplate<String, PautaResult> kafkaTemplate;

    @Mock
    private CompletableFuture completableFuture;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaProducer, "topicName", "Topico");
    }

    @Test
    @DisplayName("Deve enviar uma menssagem para o topico")
    void sendTest() {
        var result = new PautaResult(1L, 10L, 5L);
        var logCaptor = LogCaptor.forClass(KafkaProducerImpl.class);

        assertDoesNotThrow(() -> kafkaProducer.sendPautaResults(result));

        verify(kafkaTemplate, atLeastOnce()).send(anyString(), any(PautaResult.class));

        assertThat(logCaptor.getInfoLogs())
                .hasSize(2)
                .contains("Enviando mensagem para o topico.", atIndex(0))
                .contains("Mensagem enviada com sucesso.", atIndex(1));
    }
}
