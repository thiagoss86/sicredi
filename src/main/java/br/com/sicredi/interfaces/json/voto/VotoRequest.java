package br.com.sicredi.interfaces.json.voto;

import br.com.sicredi.domain.voto.VotoValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotoRequest {

    @NotBlank
    @CPF
    private String cpf;

    @NotNull
    private VotoValue votoValue;
}
