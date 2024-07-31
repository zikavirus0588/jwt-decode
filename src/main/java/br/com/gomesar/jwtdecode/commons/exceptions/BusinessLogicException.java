package br.com.gomesar.jwtdecode.commons.exceptions;

import br.com.fluentvalidator.context.Error;
import br.com.fluentvalidator.context.ValidationResult;
import br.com.fluentvalidator.exception.ValidationException;
import br.com.gomesar.jwtdecode.commons.models.BusinessLogicError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class BusinessLogicException extends ValidationException {
    private final transient ProblemDetail problemDetail;

    public BusinessLogicException(final ValidationResult validationResult) {
        super(validationResult);
        this.problemDetail = createProblemDetail(validationResult);
    }

    private static ProblemDetail createProblemDetail(final ValidationResult validationResult) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY,
            "Error when validating business logic");
        problemDetail.setTitle("Validation result error");
        problemDetail.setProperty("isValid", false);
        problemDetail.setProperty("reasons", validationResult.getErrors().stream()
            .map(BusinessLogicException::createError));
        return problemDetail;
    }

    public ProblemDetail getProblemDetail() {
        return problemDetail;
    }

    private static BusinessLogicError createError(final Error e) {
        return new BusinessLogicError.Builder().message(e.getMessage())
            .attemptedValue(e.getAttemptedValue() != null ? e.getAttemptedValue().toString() : null).build();
    }
}
