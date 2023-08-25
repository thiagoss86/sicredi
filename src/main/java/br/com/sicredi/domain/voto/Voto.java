package br.com.sicredi.domain.voto;

import br.com.sicredi.domain.pauta.Pauta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "votos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Voto implements Serializable {

    @Id
    @Column(name = "nr_id_voto", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_associate_cpf", nullable = false, unique = true, length = 14)
    private String associateCpf;

    @Column(name = "tx_voto_value", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private VotoValue votoValue;

    @ManyToOne
    @JoinColumn(name = "nr_pauta_id")
    @ToString.Exclude
    private Pauta pauta;

}
