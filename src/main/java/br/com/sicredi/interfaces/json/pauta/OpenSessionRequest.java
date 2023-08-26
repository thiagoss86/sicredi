package br.com.sicredi.interfaces.json.pauta;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "OpenSessionRequest", description = "Request para a abertura de uma sessão")
public class OpenSessionRequest {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Parameter(name = "limitTime", description = "Tempo limite para sessão ficar aberta",
            example = "02/09/2023 20:00:00", required = true)
    private LocalDateTime limitTime;
}
