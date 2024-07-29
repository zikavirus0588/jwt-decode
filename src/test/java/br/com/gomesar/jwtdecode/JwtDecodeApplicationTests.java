package br.com.gomesar.jwtdecode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(BuildProperties.class)
class JwtDecodeApplicationTests {

    @Test
    void contextLoads() {
    }

}
