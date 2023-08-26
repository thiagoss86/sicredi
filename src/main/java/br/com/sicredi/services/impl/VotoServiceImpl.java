package br.com.sicredi.services.impl;

import br.com.sicredi.converters.VotoConverter;
import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import br.com.sicredi.exeptions.ErrorCode;
import br.com.sicredi.exeptions.UnprocessableEntityException;
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
    public void registerVoto(Long pautaId, VotoRequest votoRequest) {
        log.info("Iniciando registro de votação");

        var pauta = pautaService.getPauta(pautaId);

        log.info("Iniciando votação do usuario na pauta:{}", pauta.getName());

        validUserIsAbleToVote(votoRequest.getCpf(), pauta);

        var newVote = VotoConverter.toDomain(votoRequest, pauta);

        votoRepository.save(newVote);
    }

    private void validUserIsAbleToVote(String cpf, Pauta pauta) {

        if (pauta.getSessionStatus().equals(PautaSessionStatus.CLOSED)) {
            log.error("Erro ao registrar o voto, a sessão encontra-se encerrada para a pauta:{}.", pauta.getId());
            throw new UnprocessableEntityException(ErrorCode.PAUTA_ALREADY_CLOSED);
        }

        if (Boolean.TRUE.equals(votoRepository.existsByAssociateCpfAndPauta(cpf, pauta))) {
            log.error("O usúario já realizou a votação na pauta:{}", pauta.getName());
            throw new UnprocessableEntityException(ErrorCode.ASSOCIATED_ALREADY_VOTE);
        }
    }
}
