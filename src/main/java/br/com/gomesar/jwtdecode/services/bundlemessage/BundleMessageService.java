package br.com.gomesar.jwtdecode.services.bundlemessage;

import br.com.gomesar.jwtdecode.services.bundlemessage.exceptions.BundleMessageNoSuchMessageException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class BundleMessageService implements IBundleMessageService {
    private final MessageSource messageSource;

    public BundleMessageService(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String errorCode, String... values) {
        try {
            return messageSource.getMessage(errorCode, values, Locale.getDefault());
        } catch (NoSuchMessageException exc) {
            throw new BundleMessageNoSuchMessageException(exc);
        }
    }
}
