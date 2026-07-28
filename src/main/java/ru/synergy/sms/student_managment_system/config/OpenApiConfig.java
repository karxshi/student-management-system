package ru.synergy.sms.student_managment_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentManagementOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Management System API")
                        .description(
                                "REST API для управления данными студентов Университета «Синергия»"
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Роман Патрушев")
                        )
                );
    }
}
