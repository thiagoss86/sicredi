package br.com.sicredi.domain.vote;

import br.com.sicredi.domain.schedule.Schedule;
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
@Table(name = "votes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vote implements Serializable {

    @Id
    @Column(name = "nr_id_votes", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tx_associate_cpf", nullable = false, unique = true, length = 11)
    private String associateCpf;

    @Column(name = "tx_vote_value", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private VoteValue voteValue;

    @ManyToOne
    @JoinColumn(name = "nr_schedule_id")
    @ToString.Exclude
    private Schedule schedule;

}
