package br.com.gomesar.jwtdecode.services.jwt.exceptions;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class JwtDecodeUnrecognizedClaimPropertyException extends ErrorResponseException {

    public JwtDecodeUnrecognizedClaimPropertyException(final UnrecognizedPropertyException cause) {
        super(
            HttpStatus.BAD_REQUEST,
            createProblemDetail(cause),
            cause
        );
    }

    private static ProblemDetail createProblemDetail(final UnrecognizedPropertyException cause) {
        final var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "JWT claim contain unrecognized property");
        pd.setTitle("Error when processing json from JWT claim");
        pd.setProperty("isValid", false);
        pd.setProperty("reason", "Unrecognized field('" + cause.getPropertyName() + "')." +
            " Mapped values are: " + cause.getKnownPropertyIds());
        return pd;
    }
}
