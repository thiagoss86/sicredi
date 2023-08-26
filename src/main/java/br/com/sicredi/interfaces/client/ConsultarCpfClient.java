package br.com.sicredi.interfaces.client;

import br.com.sicredi.exeptions.CpfClientException;
import br.com.sicredi.exeptions.ErrorCode;
import br.com.sicredi.interfaces.client.responses.ConsultaCpfResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class ConsultarCpfClient {


    private final String url;
    private final WebClient cpfWebClient;

    public ConsultarCpfClient(@Value("${web-client.cpf-client.url}") String url,
                              WebClient cpfWebClient) {
        this.url = url;
        this.cpfWebClient = cpfWebClient;
    }

    public ConsultaCpfResponse consultarCpf(String cpf) {
        log.info("Chamando serviço de consulta de cpf");

        try {
            var uri = UriComponentsBuilder.fromHttpUrl(this.url)
                    .pathSegment(cpf)
                    .build()
                    .toUri();

            return cpfWebClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(ConsultaCpfResponse.class)
                    .publishOn(Schedulers.boundedElastic())
                    .block();
        } catch (WebClientException ex) {
            log.error("Falha ao consultar a api de cpf");
            throw new CpfClientException(ErrorCode.CPF_CLIENT_UNAVAILABLE);
        }
    }
}
