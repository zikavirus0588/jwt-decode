package br.com.gomesar.jwtdecode.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"io.swagger", "br.com.gomesar"})
public class OpenApiConfig {

    private final BuildProperties buildProperties;

    public OpenApiConfig(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .description(
                                        """
                                             API to decode and verify JWT Claims.
                                             Key Features: Decode JWT
                                        """
                                )
                                .version(buildProperties.getVersion())
                                .title(buildProperties.getName())
                );
    }
}
