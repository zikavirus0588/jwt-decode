package br.com.gomesar.jwtdecode.handlers;

import br.com.gomesar.jwtdecode.commons.exceptions.BusinessLogicException;
import br.com.gomesar.jwtdecode.commons.models.ConstraintViolationError;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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
            cv -> new ConstraintViolationError.Builder()
                .interpolatedMessage(cv.getMessage())
                .invalidValue((String) cv.getInvalidValue())
                .build()).toList());

        final var problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            CONSTRAINT_VIOLATIONS_DETAIL
        );
        problemDetail.setTitle(CONSTRAINT_VIOLATION);
        problemDetail.setProperties(problemDetailProperties);

        return problemDetail;
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ProblemDetail handleBusinessLogicException(BusinessLogicException exc) {
        return exc.getProblemDetail();
    }
}
