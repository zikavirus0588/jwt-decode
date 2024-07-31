package br.com.gomesar.jwtdecode.services.jwt.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ClaimsDTO(
    @JsonAlias({"Role", "role"})
    String role,
    @JsonAlias({"Seed", "seed"})
    Integer seed,
    @JsonAlias({"Name", "name"})
    String name
) {
    private ClaimsDTO(final Builder builder) {
        this(builder.role, builder.seed, builder.name);
    }

    public static class Builder {
        private String role;
        private Integer seed;
        private String name;

        public Builder role(final String role) {
            this.role = role;
            return this;
        }

        public Builder seed(final Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public ClaimsDTO build() {
            return new ClaimsDTO(this);
        }

    }
}
