package br.com.sicredi.repositories;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.pauta.PautaSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    List<Pauta> findAllBySessionStatus(PautaSessionStatus sessionStatus);
}
