package br.com.gomesar.jwtdecode.controllers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {AuthorizationHeaderConstraintValidator.class})
public @interface AuthorizationHeaderValue {
    String message() default "{custom.validation.constraints.AuthorizationHeaderValue.message}";
    String initialValue() default "Bearer";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
