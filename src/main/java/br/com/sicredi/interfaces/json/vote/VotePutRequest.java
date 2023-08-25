package br.com.sicredi.interfaces.json.vote;

import br.com.sicredi.domain.vote.VoteValue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class VotePutRequest {

    @NotBlank
    @CPF
    private String cpf;

    @NotBlank
    private VoteValue voteValue;
}
