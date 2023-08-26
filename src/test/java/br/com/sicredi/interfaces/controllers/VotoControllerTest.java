package br.com.sicredi.interfaces.controllers;

import br.com.sicredi.builders.voto.VotoTestBuilder;
import br.com.sicredi.domain.voto.VotoValue;
import br.com.sicredi.interfaces.json.voto.VotoRequest;
import br.com.sicredi.services.VotoService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VotoControllerTest {

    @InjectMocks
    private VotoController controller;

    @Mock
    private VotoService service;

    private static final String CPF = "111.111.111-11";

    @Test
    @DisplayName("Deve registar um voto do associado com sucesso")
    void registerVoteTest() {
        var votoRequest = VotoTestBuilder.getVotoRequest(CPF, VotoValue.SIM);

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        doNothing().when(service).registerVoto(anyLong(), any(VotoRequest.class));

        var responseEntity = controller.registerVote(1L, votoRequest);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Voto registado com sucesso!");

        verify(service, atLeastOnce()).registerVoto(anyLong(), any(VotoRequest.class));
    }
}
