package br.com.sicredi.repositories;

import br.com.sicredi.domain.schedule.Schedule;
import br.com.sicredi.domain.schedule.ScheduleSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByIdAndSessionStatus(Long id, ScheduleSessionStatus sessionStatus);
}
