package br.com.gomesar.jwtdecode.services.validators;

import br.com.gomesar.jwtdecode.services.bundlemessage.IBundleMessageService;
import br.com.gomesar.jwtdecode.services.jwt.dto.ClaimsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ClaimDTOValidatorTest {

    @Spy
    private IBundleMessageService bundleMessageService;
    @InjectMocks
    private ClaimDTOValidator validator;

    @Test
    void testMustReturnErrorListWithSizeOf3WhenValidateClaimDTOWithNullProperties() {
        final var validationResult = validator.validate(new ClaimsDTO.Builder().build());

        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrors()).hasSize(3);
    }

    @ParameterizedTest
    @CsvSource({
        "Admin, 2, Mar1a Luisa",
        "Admin, 3, Mariaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
            " Luisaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " +
            "Silvaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        "Employee, 5, Maria Luisa",
        "Admin, 1, Maria Luisa",
        "Admin, 4, Maria Luisa",
        "Admin, 9, Maria Luisa",
        "Admin, 25, Maria Luisa",
        "Admin, 49, Maria Luisa",
    })
    void testMustReturnErrorListWithSizeOf1WhenValidateClaimDTOWithInvalidProperties(
        String role, int seed, String name
    ) {
        final var claimWithAlphanumericName = new ClaimsDTO.Builder()
            .role(role)
            .seed(seed)
            .name(name).build();

        final var validationResult = validator.validate(claimWithAlphanumericName);

        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrors()).hasSize(1);
    }

    @Test
    void testMustReturnEmptyErrorListWithWhenClientDTOIsValid() {
        final var claimWithAlphanumericName = new ClaimsDTO.Builder()
            .role("Admin")
            .seed(7841)
            .name("Toninho Araujo").build();

        final var validationResult = validator.validate(claimWithAlphanumericName);

        assertThat(validationResult.isValid()).isTrue();
        assertThat(validationResult.getErrors()).isEmpty();
    }

}