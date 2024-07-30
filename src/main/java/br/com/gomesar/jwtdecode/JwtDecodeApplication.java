package br.com.gomesar.jwtdecode;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JwtDecodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtDecodeApplication.class, args);
        ElasticApmAttacher.attach();
    }

}
