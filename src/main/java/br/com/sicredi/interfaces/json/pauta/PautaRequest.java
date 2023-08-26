package br.com.sicredi.interfaces.json.pauta;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para cria nova pauta")
public class PautaRequest {

    @NotBlank
    @Length(max = 100)
    @Parameter(description = "Nome da Pauta", example = "Pauta Modelo", required = true)
    private String name;
}
