package br.com.sicredi.domain.vote;

import br.com.sicredi.domain.Schedule.Schedule;
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
import java.util.UUID;

@Entity
@Table(name = "VOTES")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Vote implements Serializable {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ASSOCIATE_ID", nullable = false, unique = true)
    private UUID associateId;

    @Column(name = "CPF", nullable = false, unique = true)
    private String associateCpf;

    @Column(name = "VALUE", nullable = false)
    @Enumerated(EnumType.STRING)
    private VoteValue voteValue;

    @ManyToOne
    @JoinColumn(name = "ID_SCHEDULE")
    @ToString.Exclude
    private Schedule schedule;

}
