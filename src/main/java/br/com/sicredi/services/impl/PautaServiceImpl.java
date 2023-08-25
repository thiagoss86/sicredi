package br.com.sicredi.services.impl;

import br.com.sicredi.converters.PautaConverter;
import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;
import br.com.sicredi.repositories.PautaRepository;
import br.com.sicredi.services.PautaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final PautaRepository pautaRepository;

    @Override
    @Transactional
    public String createNewPauta(PautaRequest putRequest) {
        log.info("Creating a new schedule with properties: {}", putRequest.toString());

        var newPauta = PautaConverter.toDomain(putRequest);

        return String.valueOf(pautaRepository.save(newPauta).getId());
    }

    @Override
    @Transactional
    public void openNewSession(Long scheduleId, OpenSessionRequest sessionRequest) throws Exception {
        log.info("Opening a new session for pauta with id:{}", scheduleId);

        var pauta = getPauta(scheduleId);

        validatePautaStatus(pauta);

        Optional.ofNullable(sessionRequest.getLimitTime())
                .ifPresentOrElse(pauta::setLimitTime,
                        () -> pauta.setLimitTime(LocalDateTime.now().plusMinutes(1)));

        pautaRepository.save(pauta);
        log.info("Session is open with time limit of:{}", pauta.getLimitTime());
    }

    @Override
    @Transactional(readOnly = true)
    public Pauta getPauta(Long scheduleId) throws Exception {
        return pautaRepository.findById(scheduleId)
                .orElseThrow(() -> new Exception("Schedule not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pauta> getAllOpenPautas() {
        return pautaRepository.findAllBySessionStatus(PautaSessionStatus.OPEN);
    }

    @Override
    @Transactional
    public void updatePauta(Pauta pauta) {
        pautaRepository.save(pauta);
    }

    private void validatePautaStatus(Pauta pauta) throws Exception {
        log.info("Validating if schedule is already opened");

        if (pauta.getSessionStatus().equals(PautaSessionStatus.OPEN)) {
            log.error("Session for schedule with id:{} is already open", pauta.getId());
            throw new Exception("Sessão já está aberta!");
        }

        pauta.setSessionStatus(PautaSessionStatus.OPEN);
    }
}
