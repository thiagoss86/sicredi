package br.com.sicredi.repositories;

import br.com.sicredi.domain.pauta.Pauta;
import br.com.sicredi.domain.voto.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    Boolean existsByAssociateCpfAndPauta(String cpf, Pauta pauta);
}
