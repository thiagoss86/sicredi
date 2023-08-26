package br.com.sicredi.interfaces.json.voto;

import br.com.sicredi.domain.voto.VotoValue;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "VotoRequest", description = "Request para a registro de votação")
public class VotoRequest {

    @NotBlank
    @CPF
    @Parameter(name = "cpf", description = "CPF do associado", example = "111.111.111-11", required = true)
    private String cpf;

    @NotNull
    @Parameter(name = "votoValue", description = "Valor do voto do associado", example = "SIM", required = true)
    private VotoValue votoValue;
}
