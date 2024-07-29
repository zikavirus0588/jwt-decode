package br.com.gomesar.jwtdecode.commons.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record BusinessLogicError(
    String message,
    String attemptedValue
) {
    private BusinessLogicError(final Builder builder) {
        this(builder.message, builder.attemptedValue);
    }

    public static class Builder {
        String message;
        String attemptedValue;

        public Builder message(final String message) {
            this.message = message;
            return this;
        }

        public Builder attemptedValue(final String attemptedValue) {
            this.attemptedValue = attemptedValue;
            return this;
        }

        public BusinessLogicError build() {
            return new BusinessLogicError(this);
        }

    }
}
