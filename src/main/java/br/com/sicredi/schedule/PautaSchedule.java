package br.com.sicredi.schedule;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.services.PautaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class PautaSchedule {

    private final PautaService pautaService;


    @Scheduled(fixedDelay = 5000)//cron = "0 0 12", zone = "America/Sao_Paulo"
    public void verifyPautas() {
        pautaService.getAllOpenPautas()
                .forEach(this::closePautaAndSend);
    }

    private void closePautaAndSend(Pauta pauta) {
        if(pauta.getLimitTime().isBefore(LocalDateTime.now())) {
            pautaService.updatePautaAndSendMessage(pauta);
        }
    }
}
