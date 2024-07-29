package br.com.gomesar.jwtdecode.services.jwt.exceptions;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class JwtDecodeInvalidInputForClaimPropertyException extends ErrorResponseException {

    public JwtDecodeInvalidInputForClaimPropertyException(final MismatchedInputException cause) {
        super(
            HttpStatus.BAD_REQUEST,
            createProblemDetail(cause),
            cause
        );
    }

    private static ProblemDetail createProblemDetail(final MismatchedInputException cause) {
        final var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            "Invalid input for claim property");
        pd.setTitle("Mismatched input when mapping claim to pojo");
        pd.setProperty("isValid", false);
        pd.setProperty("reason", "JWT claim '" + cause.getPath().get(0).getFieldName() + "' must be of type " +
            cause.getTargetType().getSimpleName());
        return pd;
    }
}
