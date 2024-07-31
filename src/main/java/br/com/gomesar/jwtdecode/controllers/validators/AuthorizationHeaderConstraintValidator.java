package br.com.gomesar.jwtdecode.controllers.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorizationHeaderConstraintValidator implements ConstraintValidator<AuthorizationHeaderValue, String> {
    private String initialValue;

    @Override
    public void initialize(AuthorizationHeaderValue constraintAnnotation) {
        this.initialValue = constraintAnnotation.initialValue();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        final var headerValueSplit = s.split(" ");

        if (headerValueSplit.length != 2) {
            return false;
        }

        return headerValueSplit[0].startsWith(initialValue);
    }
}
