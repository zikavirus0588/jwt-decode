package br.com.gomesar.jwtdecode.commons.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AbstractApiResponse<T>(
        T data
) {
    public static class Builder<T> {
        T data;

        public Builder<T> data(final T data) {
            this.data = data;
            return this;
        }

        public AbstractApiResponse<T> build() {
            return new AbstractApiResponse<>(this.data);
        }
    }
}
