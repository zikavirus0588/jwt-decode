package br.com.gomesar.jwtdecode.services.jwt.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class JwtDecodeJsonProcessingErrorException extends ErrorResponseException {

    public JwtDecodeJsonProcessingErrorException(final JsonProcessingException cause) {
        super(
            HttpStatus.BAD_REQUEST,
            createProblemDetail(cause),
            cause
        );
    }

    private static ProblemDetail createProblemDetail(final JsonProcessingException cause) {
        final var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "Error when processing json from jwt claim");
        pd.setTitle("Unmapped Json processing error");
        pd.setProperty("isValid", false);
        pd.setProperty("reason", "Unmapped error when create Object from json: " + cause.getLocalizedMessage());
        return pd;
    }
}
