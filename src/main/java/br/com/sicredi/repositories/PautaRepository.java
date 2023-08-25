package br.com.sicredi.repositories;

import br.com.sicredi.domain.pauta.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

}
