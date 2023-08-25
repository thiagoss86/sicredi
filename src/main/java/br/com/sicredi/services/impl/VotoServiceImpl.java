package br.com.sicredi.services.impl;

import br.com.sicredi.converters.VotoConverter;
import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import br.com.sicredi.interfaces.json.voto.VotoRequest;
import br.com.sicredi.repositories.VotoRepository;
import br.com.sicredi.services.PautaService;
import br.com.sicredi.services.VotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VotoServiceImpl implements VotoService {

    private final VotoRepository votoRepository;

    private final PautaService pautaService;

    @Override
    @Transactional
    public void registerVoto(Long scheduleId, VotoRequest votoRequest) throws Exception {
        log.info("Registering vote for user:{} on schedule:{}", votoRequest.getCpf(), scheduleId);

        var schedule = pautaService.getPauta(scheduleId);

        validUserIsAbleToVote(votoRequest.getCpf(), schedule);

        var newVote = VotoConverter.toDomain(votoRequest, schedule);

        votoRepository.save(newVote);
    }

    private void validUserIsAbleToVote(String cpf, Pauta pauta) throws Exception {

        if (pauta.getSessionStatus().equals(PautaSessionStatus.CLOSED)) {
            log.error("Erro ao registrar o voto, a sessão com id:{} encontra fechada", pauta.getId());

            throw new Exception("Sessão já se encontra encerrada");
        }

        if (Boolean.TRUE.equals(votoRepository.existsByAssociateCpfAndPauta(cpf, pauta))) {
            log.error("Usuário de cpf:{} já votou nesta sessão de id:{}", cpf, pauta.getId());

            throw new Exception("Usuario já realizou a votação nesta sessão");
        }
    }
}
