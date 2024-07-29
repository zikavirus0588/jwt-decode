package br.com.gomesar.jwtdecode.services.jwt;

import br.com.gomesar.jwtdecode.services.jwt.dto.ClaimsDTO;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeInvalidInputForClaimPropertyException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeUnrecognizedClaimPropertyException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeInvalidTokenException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeJsonProcessingErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private JwtService jwtService;

    @Test
    void testMustReturnClaimsDTOWhenDecodeValidJwt() {
        final var jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25p" +
                "bmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        final var decoded = jwtService.decode(jwt);
        Assertions.assertThat(decoded).isNotNull();
    }

    @Test
    void testMustThrowJwtDecodeInvalidTokenExceptionWhenDecodeInvalidJwt() {
        final var jwt = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25p" +
                "bmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
        assertThrows(JwtDecodeInvalidTokenException.class, () -> jwtService.decode(jwt), "");
    }

    @Test
    void testMustThrowJwtDecodeUnrecognizedClaimPropertyExceptionWhenDecodeInvalidJwt() {
        final var jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lI" +
                "joiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";
        assertThrows(JwtDecodeUnrecognizedClaimPropertyException.class, () -> jwtService.decode(jwt), "");
    }

    @Test
    void testMustThrowJwtDecodeInvalidInputForClaimPropertyExceptionWhenDecodeWithInvalidJWT() {
        final var jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjp7InppY2EiOiIxIn0sIlNlZWQiOlsiNzg0MSJdLCJOYW1lIjoiVG9uaW5ob" +
            "yBBcmF1am8ifQ.EeHHGSURJsxeYZTDd9WO-I7WN6fzm4SkxLvY5-D9y94";
        assertThrows(JwtDecodeInvalidInputForClaimPropertyException.class, () -> jwtService.decode(jwt), "");
    }

    @Test
    void testMustJwtDecodeJsonProcessingErrorExceptionWhenDecodeInvalidJwt() throws JsonProcessingException {
        final var jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lI" +
                "joiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";

        final var mockObjectMapper = mock(ObjectMapper.class);
        when(mockObjectMapper.readValue(anyString(), eq(ClaimsDTO.class)))
            .thenThrow(new JsonProcessingException("Error") {});
        final var service = new JwtService(mockObjectMapper);
        assertThrows(JwtDecodeJsonProcessingErrorException.class, () -> service.decode(jwt), "");
    }
}