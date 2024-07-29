package br.com.gomesar.jwtdecode.commons.models;

public enum EErrorCode {

    CLAIM_NAME_REQUIRED("claim.name.required"),
    CLAIM_SEED_REQUIRED("claim.seed.required"),
    CLAIM_ROLE_REQUIRED("claim.role.required"),
    CLAIM_NAME_GREATER_THAN_MAX_ALLOWED("claim.name.size.greater.than.max.allowed"),
    CLAIM_NAME_MUST_CONTAIN_ONLY_ALPHA_CHARS("claim.name.must.contain.only.alpha.chars"),
    CLAIM_ROLE_MUST_BE_ONE_OF("claim.role.must.be.one.of"),
    CLAIM_SEED_MUST_BE_A_PRIME_NUMBER("claim.seed.must.be.prime.number");

    public final String code;

    EErrorCode(final String code) {
        this.code = code;
    }
}
