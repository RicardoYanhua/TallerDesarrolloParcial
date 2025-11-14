package com.yanhua.ms.Operacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Operacion")
                        .version("1.0.0")
                        .description("API del microservicio de Operacion"))
                .servers(List.of(
                        new Server().url("http://localhost:87").description("Microservicio Operacion")));

    }
}
