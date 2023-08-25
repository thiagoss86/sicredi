package br.com.sicredi.interfaces.json.pauta;

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
public class PautaRequest {

    @NotBlank
    @Length(max = 100)
    private String name;
}