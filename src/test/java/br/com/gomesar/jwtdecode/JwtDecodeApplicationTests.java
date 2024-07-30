package br.com.gomesar.jwtdecode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(BuildProperties.class)
@ActiveProfiles("tst")
class JwtDecodeApplicationTests {

    @Test
    void contextLoads() {
    }

}
