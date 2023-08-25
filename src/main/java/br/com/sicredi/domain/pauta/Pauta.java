package br.com.sicredi.domain.pauta;

import br.com.sicredi.domain.voto.Voto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pautas")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Where(clause = "tx_session_status != 'TERMINATED'")
public class Pauta implements Serializable {

    @Id
    @Column(name = "nr_id_pauta", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Voto> votos = new ArrayList<>();

    @ColumnDefault("CLOSED")
    @Column(name = "tx_session_status", length = 10)
    @Enumerated(EnumType.STRING)
    private PautaSessionStatus sessionStatus;

    @Column(name = "dt_limit_time")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime limitTime;

}
