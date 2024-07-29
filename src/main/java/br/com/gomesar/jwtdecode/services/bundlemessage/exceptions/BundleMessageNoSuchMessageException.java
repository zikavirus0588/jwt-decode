package br.com.gomesar.jwtdecode.services.bundlemessage.exceptions;

import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class BundleMessageNoSuchMessageException extends ErrorResponseException {

    public BundleMessageNoSuchMessageException(final NoSuchMessageException cause) {
        super(
            HttpStatus.INTERNAL_SERVER_ERROR,
            createProblemDetail(cause),
            cause
        );
    }

    private static ProblemDetail createProblemDetail(final NoSuchMessageException cause) {
        final var pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
            cause.getMessage());
        pd.setTitle("Bundle message error");
        return pd;
    }
}
