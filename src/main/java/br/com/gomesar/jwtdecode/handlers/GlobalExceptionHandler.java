package br.com.gomesar.jwtdecode.handlers;

import br.com.gomesar.jwtdecode.commons.models.ConstraintViolationError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String CONSTRAINT_VIOLATIONS_DETAIL = "request parameter contains a different value" +
            " than expected";
    public static final String CONSTRAINT_VIOLATION = "Constraint Violation";
    public static final String VIOLATIONS = "violations";

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ProblemDetail handleMissingRequestHeaderException(final MissingRequestHeaderException exception) {
        return exception.getBody();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(final ConstraintViolationException exception) {
        final var problemDetailProperties = new HashMap<String, Object>();
        problemDetailProperties.put(VIOLATIONS, exception.getConstraintViolations().stream().map(
                cv -> new ConstraintViolationError(cv.getMessage(), (String) cv.getInvalidValue())).toList());

        final var problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(400),
                CONSTRAINT_VIOLATIONS_DETAIL
        );
        problemDetail.setTitle(CONSTRAINT_VIOLATION);
        problemDetail.setProperties(problemDetailProperties);

        return problemDetail;
    }
}
