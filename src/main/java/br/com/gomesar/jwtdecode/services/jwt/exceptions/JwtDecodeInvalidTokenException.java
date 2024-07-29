package br.com.gomesar.jwtdecode.services.jwt.exceptions;

import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class JwtDecodeInvalidTokenException extends ErrorResponseException {

    public JwtDecodeInvalidTokenException(final InvalidJwtException cause) {
        super(
            HttpStatus.BAD_REQUEST,
            createProblemDetail(cause),
            cause
        );
    }

    private static ProblemDetail createProblemDetail(final InvalidJwtException cause) {
        final var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "Unable to decode JWT");
        pd.setTitle("Invalid JWT");
        pd.setProperty("isValid", false);
        pd.setProperty("reason", cause.getOriginalMessage());
        return pd;
    }
}
