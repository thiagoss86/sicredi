package br.com.sicredi.services.impl;

import br.com.sicredi.builders.pauta.PautaTestBuilder;
import br.com.sicredi.builders.voto.VotoTestBuilder;
import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.voto.Voto;
import br.com.sicredi.domain.voto.VotoValue;
import br.com.sicredi.exeptions.UnprocessableEntityException;
import br.com.sicredi.repositories.VotoRepository;
import br.com.sicredi.services.PautaService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceImplTest {

    @InjectMocks
    private VotoServiceImpl service;

    @Mock
    private PautaService pautaService;

    @Mock
    private VotoRepository repository;

    private static final String CPF = "111.111.111-11";
    private static final String PAUTA_NAME = "Pauta Unit Test";

    @Test
    @DisplayName("Deve registrar o voto do associado com sucesso")
    void registerVotoSuccessTest() {
        var request = VotoTestBuilder.getVotoRequest(CPF, VotoValue.SIM);
        var pauta = PautaTestBuilder.getPautaDomainWithOpenSession(PAUTA_NAME);

        when(pautaService.getPauta(anyLong()))
                .thenReturn(pauta);

        when(repository.existsByAssociateCpfAndPauta(anyString(), any(Pauta.class)))
                .thenReturn(Boolean.FALSE);

        service.registerVoto(1L, request);

        ArgumentCaptor<Voto> captor = ArgumentCaptor.forClass(Voto.class);

        verify(repository).save(captor.capture());

        var voto = captor.getValue();

        assertThat(voto).isNotNull();
        assertThat(voto.getVotoValue()).isEqualTo(VotoValue.SIM);
        assertThat(voto.getAssociateCpf()).isEqualTo(CPF);
        assertThat(voto.getPauta()).isEqualTo(pauta);

    }

    @Test
    @DisplayName("Deve retornar uma exceção ao tentar realizar um voto a uma pauta fechada")
    void registeVotoWithClosedPautaTest() {
        var request = VotoTestBuilder.getVotoRequest(CPF, VotoValue.SIM);
        var pauta = PautaTestBuilder.getPautaDomain(PAUTA_NAME);
        var logCaptor = LogCaptor.forClass(VotoServiceImpl.class);

        when(pautaService.getPauta(anyLong()))
                .thenReturn(pauta);

        assertThatThrownBy(() -> service.registerVoto(1L, request))
                .isInstanceOf(UnprocessableEntityException.class);
        assertThat(logCaptor.getErrorLogs())
                .hasSize(1)
                .contains(MessageFormat.format(
                        "Erro ao registrar o voto, a sessão encontra-se encerrada para a pauta:{0}.", pauta.getId()));
    }

    @Test
    @DisplayName("Deve retornar uma exceção quando um associado tentar votar uma segunda vez na mesma pauta")
    void registeVotoWithSameAssociateVote() {
        var request = VotoTestBuilder.getVotoRequest(CPF, VotoValue.NAO);
        var pauta = PautaTestBuilder.getPautaDomainWithOpenSession(PAUTA_NAME);
        var logCaptor = LogCaptor.forClass(VotoServiceImpl.class);

        when(pautaService.getPauta(anyLong()))
                .thenReturn(pauta);

        when(repository.existsByAssociateCpfAndPauta(anyString(), any(Pauta.class)))
                .thenReturn(Boolean.TRUE);

        assertThatThrownBy(() -> service.registerVoto(1L, request))
                .isInstanceOf(UnprocessableEntityException.class);
        assertThat(logCaptor.getErrorLogs())
                .hasSize(1)
                .contains(MessageFormat.format("O usúario já realizou a votação na pauta:{0}", pauta.getName()));
    }
}
