package dev.dorigo.financecontrol.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenAPI(){
        Info info = new Info();
        Contact contact = new Contact();
        contact.setUrl("https://github.com/dorigodev");
        contact.setName("Murilo Dorigo");
        info.title("Finance Control API");
        info.version("1.0");
        info.description("Aplicação para gerenciamento financeiro de entrada e saida de custos.");
        info.contact(contact);

        Components components = new Components();
        return new OpenAPI().info(info);
    }
}
