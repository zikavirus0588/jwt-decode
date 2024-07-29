package br.com.gomesar.jwtdecode.services.token;

import br.com.fluentvalidator.context.Error;
import br.com.fluentvalidator.context.ValidationResult;
import br.com.gomesar.jwtdecode.commons.exceptions.BusinessLogicException;
import br.com.gomesar.jwtdecode.services.jwt.IJwtService;
import br.com.gomesar.jwtdecode.services.jwt.dto.ClaimsDTO;
import br.com.gomesar.jwtdecode.services.validators.ClaimDTOValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private IJwtService jwtService;
    @Mock
    private ClaimDTOValidator claimsDTOValidator;
    @InjectMocks
    private TokenService service;

    @Test
    void testMustThrowBusinessLogicExceptionWhenDecodeTokenAndWithInvalidClaim() throws NoSuchFieldException,
        IllegalAccessException {
        final var jwtTokenWithInvalidClaimRole = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRW1wbG95ZWUiLCJTZWVkIjoiNzg0" +
            "MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.jishzkSPBs5PPs8vq35SOLrc9CG5lS1Ig3kBpBWiuGk";
        final var claimDTO = new ClaimsDTO.Builder().name("Toninho Araujo").seed(7841).role("Employee").build();

        doReturn(claimDTO).when(jwtService).decode(any());

        final var validationResultMock = mock(ValidationResult.class);

        doReturn(
            List.of(Error.create("some field", "some message", "some code", null))
        ).when(validationResultMock).getErrors();

        final var field = validationResultMock.getClass().getDeclaredField("valid");
        field.setAccessible(true);
        field.set(validationResultMock, false);

        final var businessLogicException = new BusinessLogicException(validationResultMock);

        doThrow(businessLogicException).when(validationResultMock).isInvalidThrow(any());

        doReturn(validationResultMock).when(claimsDTOValidator).validate(any(ClaimsDTO.class));

        assertThrows(BusinessLogicException.class, () -> service.decodeToken(jwtTokenWithInvalidClaimRole), "");
    }

    @Test
    void testMustReturnTrueWhenDecodeAValidJWT() throws NoSuchFieldException,
        IllegalAccessException {
        final var jwtTokenWithInvalidClaimRole = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsI" +
            "k5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.jcnLdGdb11LsIxJ3JMPDQFqzs1aZFr5t75s8QgNxtxY";
        final var claimDTO = new ClaimsDTO.Builder().name("Toninho Araujo").seed(7841).role("Admin").build();

        doReturn(claimDTO).when(jwtService).decode(any());

        final var validationResultMock = mock(ValidationResult.class);

        final var field = validationResultMock.getClass().getDeclaredField("valid");
        field.setAccessible(true);
        field.set(validationResultMock, true);

        doNothing().when(validationResultMock).isInvalidThrow(any());

        doReturn(validationResultMock).when(claimsDTOValidator).validate(any(ClaimsDTO.class));

        assertThat(service.decodeToken(jwtTokenWithInvalidClaimRole)).isTrue();
    }
}