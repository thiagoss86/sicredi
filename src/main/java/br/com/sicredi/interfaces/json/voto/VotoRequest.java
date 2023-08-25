package br.com.sicredi.interfaces.json.voto;

import br.com.sicredi.domain.voto.VotoValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class VotoRequest {

    @NotBlank
    @CPF
    private String cpf;

    @NotNull
    private VotoValue votoValue;
}
