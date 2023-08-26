package br.com.sicredi.configurations;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Desafio Sicredi - API")
                        .description("API para cadastro de pauta e abertura de sessão de votação")
                        .version("1.0")
                        .termsOfService("Termo de uso: Open Source")
                ).externalDocs(new ExternalDocumentation()
                        .description("Thiago Siqueira dos Santos")
                        .url("https://github.com/thiagoss86"));
    }
}
