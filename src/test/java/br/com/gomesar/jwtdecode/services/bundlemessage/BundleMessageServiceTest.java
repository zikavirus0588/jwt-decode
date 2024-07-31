package br.com.gomesar.jwtdecode.services.bundlemessage;

import br.com.gomesar.jwtdecode.services.bundlemessage.exceptions.BundleMessageNoSuchMessageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class BundleMessageServiceTest {

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private BundleMessageService bundleMessageService;

    @Test
    void testMustReturnMessageWhenFindMessageInBundleResourceForCodeProvided() {
        final var code = "some.code";
        final var params = "with params";
        final var expectedMessage = "message with params";
        doReturn(expectedMessage).when(messageSource).getMessage(anyString(), any(String[].class),
            any(Locale.class));
        assertThat(bundleMessageService.getMessage(code, params)).isEqualTo(expectedMessage);
    }

    @Test
    void testMustThrowBundleMessageNoSuchMessageExceptionWhenNotFindMessageInBundleResourceForCodeProvided() {
        final var code = "some.not.existent.code";
        doThrow(NoSuchMessageException.class).when(messageSource).getMessage(anyString(), any(String[].class),
            any(Locale.class));
        assertThrows(BundleMessageNoSuchMessageException.class,
            () -> bundleMessageService.getMessage(code), "");
    }
}