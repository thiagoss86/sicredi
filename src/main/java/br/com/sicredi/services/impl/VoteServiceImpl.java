package br.com.sicredi.services.impl;

import br.com.sicredi.converters.VoteConverter;
import br.com.sicredi.domain.schedule.Schedule;
import br.com.sicredi.domain.schedule.ScheduleSessionStatus;
import br.com.sicredi.interfaces.json.vote.VotePutRequest;
import br.com.sicredi.repositories.VoteRepository;
import br.com.sicredi.services.ScheduleService;
import br.com.sicredi.services.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    private final ScheduleService scheduleService;

    @Override
    @Transactional
    public void registerVote(Long scheduleId, VotePutRequest votePutRequest) throws Exception {
        log.info("Registering vote for user:{} on schedule:{}", votePutRequest.getCpf(), scheduleId);

        var schedule = scheduleService.getSchedule(scheduleId);

        validUserIsAbleToVote(votePutRequest.getCpf(), schedule);

        var newVote = VoteConverter.toDomain(votePutRequest, schedule);

        voteRepository.save(newVote);
    }

    private void validUserIsAbleToVote(String cpf, Schedule schedule) throws Exception {

        if (schedule.getSessionStatus().equals(ScheduleSessionStatus.CLOSED)) {
            log.error("Erro ao registrar o voto, a sessão com id:{} encontra fechada", schedule.getId());

            throw new Exception("Sessão já se encontra encerrada");
        }

        if (Boolean.TRUE.equals(voteRepository.existsByAssociateCpfAndSchedule(cpf, schedule))) {
            log.error("Usuário de cpf:{} já votou nesta sessão de id:{}", cpf, schedule.getId());

            throw new Exception("Usuario já realizou a votação nesta sessão");
        }
    }
}
