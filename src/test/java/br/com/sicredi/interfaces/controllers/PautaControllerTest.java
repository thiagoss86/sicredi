package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.builders.pauta.OpenSessionTestBuilder;
import br.com.sicredi.builders.pauta.PautaTestBuilder;
import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;
import br.com.sicredi.services.PautaService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PautaControllerTest {

    @InjectMocks
    private PautaController controller;

    @Mock
    private PautaService pautaService;

    private static final String PAUTA_NAME = RandomStringUtils.randomAlphabetic(15);
    private static final LocalDateTime SESSION_TIME = LocalDateTime.now().plusMinutes(10);

    @Test
    @DisplayName("Deve cadastrar uma nova pauta")
    void postPautaTest() {
        var pautaRequest = PautaTestBuilder.getPautaRequest(PAUTA_NAME);
        var pauta = PautaTestBuilder.getPautaDomain(PAUTA_NAME);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(pautaService.createNewPauta(any(PautaRequest.class)))
                .thenReturn(pauta);

        var responseEntity = controller.postPauta(pautaRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Deve abrir uma nova sessão")
    void openSessionTest() {
        var sessionRequest = OpenSessionTestBuilder.getOpenSessionRequest(SESSION_TIME);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        doNothing().when(pautaService).openNewSession(anyLong(), any(OpenSessionRequest.class));

        var responseEntity = controller.openSession(1L, sessionRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Sessão aberta com sucesso!");

        verify(pautaService, atLeastOnce()).openNewSession(anyLong(), any(OpenSessionRequest.class));
    }
}
