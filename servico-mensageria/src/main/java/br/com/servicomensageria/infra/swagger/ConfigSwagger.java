package br.com.servicomensageria.infra.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigSwagger {

    @Bean
    public OpenAPI crudUsuarioApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Serviço de Mensageria")
                        .description("CRUD para usuario implementando RabbitMQ.")
                        .contact(new Contact()
                                .name(" :: Alessandra Bergamasque / Carlos Jessiel")
                                .email("email@email.com"))
                        .license(new License()
                                .name("Apache License Version 2.0")
                                .url("https://www.apache.org/licenses/")));

    }
}


