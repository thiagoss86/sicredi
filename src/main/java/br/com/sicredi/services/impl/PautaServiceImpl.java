package br.com.sicredi.services.impl;

import br.com.sicredi.converters.PautaConverter;
import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import br.com.sicredi.domain.voto.VotoValue;
import br.com.sicredi.exeptions.ErrorCode;
import br.com.sicredi.exeptions.NotFoundException;
import br.com.sicredi.exeptions.UnprocessableEntityException;
import br.com.sicredi.interfaces.json.kafka.PautaResult;
import br.com.sicredi.interfaces.json.pauta.OpenSessionRequest;
import br.com.sicredi.interfaces.json.pauta.PautaRequest;
import br.com.sicredi.kafka.KafkaProducer;
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
    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional
    public String createNewPauta(PautaRequest putRequest) {
        log.info("Criando uma nova pauta com o nome: {}", putRequest.getName());

        var newPauta = PautaConverter.toDomain(putRequest);

        return String.valueOf(pautaRepository.save(newPauta).getId());
    }

    @Override
    @Transactional
    public void openNewSession(Long scheduleId, OpenSessionRequest sessionRequest) {
        log.info("Iniciando abertura da sessão.");

        var pauta = getPauta(scheduleId);

        log.info("Pauta:{}, abrindo sessão", pauta.getName());

        validatePautaStatus(pauta);

        Optional.ofNullable(sessionRequest.getLimitTime())
                .ifPresentOrElse(pauta::setLimitTime,
                        () -> pauta.setLimitTime(LocalDateTime.now().plusMinutes(1)));

        pautaRepository.save(pauta);
        log.info("Sessão aberta para a pauta:{} com o data limite para:{}", pauta.getName(), pauta.getLimitTime());
    }

    @Override
    @Transactional(readOnly = true)
    public Pauta getPauta(Long pautaId) {
        log.debug("Buscando pauta com o id:{}", pautaId);

        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAUTA_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pauta> getAllOpenPautas() {
        log.info("Buscando todas as pautas abertas.");

        return pautaRepository.findAllBySessionStatus(PautaSessionStatus.OPEN);
    }

    @Override
    @Transactional
    public void updatePautaAndSendMessage(Pauta pauta) {
        log.info("Fechando a pauta:{} e contabilizando os votos.", pauta.getName());

        var pautaResult = new PautaResult(
                pauta.getId(),
                pauta.getVotos().stream().filter(voto -> voto.getVotoValue().equals(VotoValue.SIM)).count(),
                pauta.getVotos().stream().filter(voto -> voto.getVotoValue().equals(VotoValue.NAO)).count()
        );

        pauta.setSessionStatus(PautaSessionStatus.TERMINATED);
        pautaRepository.save(pauta);

        log.info("Pauta encerrada com sucesso!");
        kafkaProducer.sendPautaResults(pautaResult);
    }

    private void validatePautaStatus(Pauta pauta) {
        log.info("Validando se a pauta:{} está aberta", pauta.getName());

        if (pauta.getSessionStatus().equals(PautaSessionStatus.OPEN)) {
            log.error("Já existe uma sessão aberta para esta pauta:{}", pauta.getName());
            throw new UnprocessableEntityException(ErrorCode.PAUTA_ALREADY_OPEN);
        }

        pauta.setSessionStatus(PautaSessionStatus.OPEN);
    }
}
