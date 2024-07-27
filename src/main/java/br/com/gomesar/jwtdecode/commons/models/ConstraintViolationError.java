package br.com.gomesar.jwtdecode.commons.models;

public record ConstraintViolationError(
        String interpolatedMessage,
        String invalidValue
) {
}
