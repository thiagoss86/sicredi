package br.com.sicredi.repositories;

import br.com.sicredi.domain.schedule.Schedule;
import br.com.sicredi.domain.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Boolean existsByAssociateCpfAndSchedule(String cpf, Schedule schedule);
}
