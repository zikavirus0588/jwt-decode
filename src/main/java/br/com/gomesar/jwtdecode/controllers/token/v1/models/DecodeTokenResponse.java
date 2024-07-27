package br.com.gomesar.jwtdecode.controllers.token.v1.models;

public record DecodeTokenResponse(
        boolean isValidToken
) {
    public static class Builder {
        boolean isValidToken;

        public Builder isValidToken(final boolean isValidToken) {
            this.isValidToken = isValidToken;
            return this;
        }

        public DecodeTokenResponse build() {
            return new DecodeTokenResponse(this.isValidToken);
        }
    }
}
