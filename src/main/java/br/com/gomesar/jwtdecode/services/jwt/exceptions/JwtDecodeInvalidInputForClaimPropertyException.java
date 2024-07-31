package br.com.gomesar.jwtdecode.services.jwt.exceptions;

import br.com.gomesar.jwtdecode.commons.models.BusinessLogicError;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.util.List;

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
        pd.setProperty("reasons", List.of(new BusinessLogicError.Builder()
            .message("JWT claim '" + cause.getPath().get(0).getFieldName() + "' must be of type " +
                     cause.getTargetType().getSimpleName())
            .build()));
        return pd;
    }
}
