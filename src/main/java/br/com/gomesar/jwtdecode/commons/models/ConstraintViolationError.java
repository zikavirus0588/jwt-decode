package br.com.gomesar.jwtdecode.commons.models;

public record ConstraintViolationError(
    String interpolatedMessage,
    String invalidValue
) {
    private ConstraintViolationError(final Builder builder) {
        this(builder.interpolatedMessage, builder.invalidValue);
    }

    public static class Builder {
        String interpolatedMessage;
        String invalidValue;

        public Builder interpolatedMessage(final String interpolatedMessage) {
            this.interpolatedMessage = interpolatedMessage;
            return this;
        }

        public Builder invalidValue(final String invalidValue) {
            this.invalidValue = invalidValue;
            return this;
        }

        public ConstraintViolationError build() {
            return new ConstraintViolationError(this);
        }

    }
}
