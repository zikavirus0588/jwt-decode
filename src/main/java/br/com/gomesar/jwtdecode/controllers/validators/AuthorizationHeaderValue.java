package br.com.gomesar.jwtdecode.controllers.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
    ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {AuthorizationHeaderConstraintValidator.class})
public @interface AuthorizationHeaderValue {
    String message() default "{custom.validation.constraints.AuthorizationHeaderValue.message}";

    String initialValue() default "Bearer";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
