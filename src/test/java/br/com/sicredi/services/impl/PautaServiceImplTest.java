package br.com.sicredi.services.impl;

import br.com.sicredi.builders.pauta.OpenSessionTestBuilder;
import br.com.sicredi.builders.pauta.PautaTestBuilder;
import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import br.com.sicredi.exeptions.NotFoundException;
import br.com.sicredi.exeptions.UnprocessableEntityException;
import br.com.sicredi.interfaces.json.kafka.PautaResult;
import br.com.sicredi.kafka.KafkaProducer;
import br.com.sicredi.repositories.PautaRepository;
import nl.altindag.log.LogCaptor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceImplTest {

    @InjectMocks
    private PautaServiceImpl service;

    @Mock
    private PautaRepository repository;

    @Mock
    private KafkaProducer kafkaProducer;

    private static String NAME_FOR_PAUTA;
    private static LocalDateTime SESSION_TIME;

    @BeforeAll
    static void setUp() {
        NAME_FOR_PAUTA = RandomStringUtils.randomAlphabetic(15);
        SESSION_TIME = LocalDateTime.now().plusMinutes(10);
    }


    @Test
    @DisplayName("Deve registrar uma nova pauta com sucesso")
    void createPautaSuccess() {
        var request = PautaTestBuilder.getPautaRequest(NAME_FOR_PAUTA);

        service.createNewPauta(request);

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);

        verify(repository).save(captor.capture());

        var entity = captor.getValue();

        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo(NAME_FOR_PAUTA);
        assertThat(entity.getSessionStatus()).isEqualTo(PautaSessionStatus.CLOSED);
    }

    @Test
    @DisplayName("Deve abrir uma nova sessão para a pauta com sucesso")
    void openSessionSuccessTest() {
        var request = OpenSessionTestBuilder.getOpenSessionRequest(SESSION_TIME);
        var entity = PautaTestBuilder.getPautaDomain(NAME_FOR_PAUTA);

        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(entity));

        service.openNewSession(1L, request);

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);

        verify(repository).save(captor.capture());

        var savedEntity = captor.getValue();

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getName()).isEqualTo(NAME_FOR_PAUTA);
        assertThat(savedEntity.getLimitTime()).isEqualTo(SESSION_TIME);
        assertThat(savedEntity.getSessionStatus()).isEqualTo(PautaSessionStatus.OPEN);
    }

    @Test
    @DisplayName("Deve abrir uma nova sessão para a pauta com sucesso sem expecificar o limite de tempo")
    void openSessionSuccessWithoutTimeLimitTest() {
        var request = OpenSessionTestBuilder.getOpenSessionRequest(null);
        var entity = PautaTestBuilder.getPautaDomain(NAME_FOR_PAUTA);

        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(entity));

        service.openNewSession(1L, request);

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);

        verify(repository).save(captor.capture());

        var savedEntity = captor.getValue();

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getName()).isEqualTo(NAME_FOR_PAUTA);
        assertThat(savedEntity.getLimitTime()).isBefore(SESSION_TIME);
        assertThat(savedEntity.getSessionStatus()).isEqualTo(PautaSessionStatus.OPEN);
    }

    @Test
    @DisplayName("Deve retornar uma exceção ao tentar abrir uma sessão para pauta com sessao aberta")
    void openSessionWithExistentOpenedSessionTest() {
        var request = OpenSessionTestBuilder.getOpenSessionRequest(SESSION_TIME);
        var entity = PautaTestBuilder.getPautaDomainWithOpenSession(NAME_FOR_PAUTA);
        var logCaptor = LogCaptor.forClass(PautaServiceImpl.class);

        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.openNewSession(1L, request))
                .isInstanceOf(UnprocessableEntityException.class);
        assertThat(logCaptor.getErrorLogs())
                .hasSize(1)
                .contains(MessageFormat.format("Já existe uma sessão aberta para esta pauta:{0}", NAME_FOR_PAUTA));
    }

    @Test
    @DisplayName("Deve retornar uma exceção de não encontrada ao tentar abrir uma sessão para uma pauta inexistente")
    void openSessionNotFoundTest() {
        var request = OpenSessionTestBuilder.getOpenSessionRequest(SESSION_TIME);
        var logCaptor = LogCaptor.forClass(PautaServiceImpl.class);

        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.openNewSession(1L, request))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Deve retornar uma lista com pautas abertas")
    void getAllOpenedPautaTest() {
        when(repository.findAllBySessionStatus(PautaSessionStatus.OPEN))
                .thenReturn(List.of(PautaTestBuilder.getPautaDomainWithOpenSession(NAME_FOR_PAUTA)));

        var response = service.getAllOpenPautas();

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getName()).isEqualTo(NAME_FOR_PAUTA);
    }

    @Test
    @DisplayName("Deve encerrar a pauta e enviar ao kafka")
    void updateAndSendPautaTest() {
        var pauta = PautaTestBuilder.getPautaWithVotes(NAME_FOR_PAUTA);

        service.updatePautaAndSendMessage(pauta);

        ArgumentCaptor<Pauta> captor = ArgumentCaptor.forClass(Pauta.class);

        verify(repository).save(captor.capture());
        verify(kafkaProducer, atLeastOnce()).sendPautaResults(any(PautaResult.class));

        var terminatedPauta = captor.getValue();

        assertThat(terminatedPauta).isNotNull();
        assertThat(terminatedPauta.getSessionStatus()).isEqualTo(PautaSessionStatus.TERMINATED);
    }

}
