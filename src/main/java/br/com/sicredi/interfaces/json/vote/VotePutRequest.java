package br.com.sicredi.interfaces.json.vote;

import br.com.sicredi.domain.vote.VoteValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class VotePutRequest {

    @NotBlank
    @CPF
    private String cpf;

    @NotNull
    private VoteValue voteValue;
}
