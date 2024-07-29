package br.com.gomesar.jwtdecode.services.validators;

import br.com.fluentvalidator.AbstractValidator;
import br.com.fluentvalidator.predicate.LogicalPredicate;
import br.com.fluentvalidator.predicate.StringPredicate;
import br.com.gomesar.jwtdecode.commons.models.EErrorCode;
import br.com.gomesar.jwtdecode.services.bundlemessage.IBundleMessageService;
import br.com.gomesar.jwtdecode.services.jwt.dto.ClaimsDTO;
import br.com.gomesar.jwtdecode.services.jwt.enums.ERole;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Predicate;

import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;

@Component
public class ClaimDTOValidator extends AbstractValidator<ClaimsDTO> {
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_SEED = "seed";
    public static final String PROPERTY_ROLE = "role";
    public static final int MAX_SIZE_NAME = 256;
    private final IBundleMessageService bundleMessageService;

    public ClaimDTOValidator(final IBundleMessageService bundleMessageService) {
        this.bundleMessageService = bundleMessageService;
    }

    private static boolean isPrime(final Integer seed) {
        if (seed == 2 || seed == 3) {
            return true;
        }

        if (seed <= 1 || seed % 2 == 0 || seed % 3 == 0) {
            return false;
        }

        // To check through all numbers of the form 6k ± 1
        for (int i = 5; i * i <= seed; i += 6) {
            if (seed % i == 0 || seed % (i + 2) == 0)
                return false;
        }

        return true;
    }

    @Override
    public void rules() {
        required(ClaimsDTO::name, EErrorCode.CLAIM_NAME_REQUIRED, PROPERTY_NAME);
        required(ClaimsDTO::seed, EErrorCode.CLAIM_SEED_REQUIRED, PROPERTY_SEED);
        required(ClaimsDTO::role, EErrorCode.CLAIM_ROLE_REQUIRED, PROPERTY_ROLE);

        nameMustBeOnlyAlphaCharacters();
        nameSizeMustBeLessOrEqualToMaxAllowed();

        roleMustBeOneOfTheAllowedRoles();

        seedMustBeAPrimeNumber();
    }

    private void seedMustBeAPrimeNumber() {
        ruleFor(ClaimsDTO::seed)
            .must(LogicalPredicate.isTrue(ClaimDTOValidator::isPrime))
            .when(Predicate.not(nullValue()))
            .withMessage(bundleMessageService.getMessage(EErrorCode.CLAIM_SEED_MUST_BE_A_PRIME_NUMBER.code))
            .withFieldName(PROPERTY_SEED)
            .withAttempedValue(ClaimsDTO::seed)
            .withCode(EErrorCode.CLAIM_SEED_MUST_BE_A_PRIME_NUMBER.code);
    }

    private void roleMustBeOneOfTheAllowedRoles() {
        ruleFor(ClaimsDTO::role)
            .must(StringPredicate.stringInCollection(ERole.getAllowedRoles()))
            .when(Predicate.not(nullValue()))
            .withMessage(dto -> bundleMessageService.getMessage(EErrorCode.CLAIM_ROLE_MUST_BE_ONE_OF.code,
                ERole.getAllowedRoles().toString()))
            .withFieldName(PROPERTY_ROLE)
            .withAttempedValue(ClaimsDTO::role)
            .withCode(EErrorCode.CLAIM_ROLE_MUST_BE_ONE_OF.code);
    }

    private void nameSizeMustBeLessOrEqualToMaxAllowed() {
        ruleFor(ClaimsDTO::name)
            .must(StringPredicate.stringSizeLessThanOrEqual(MAX_SIZE_NAME))
            .when(Predicate.not(nullValue()))
            .withMessage(bundleMessageService.getMessage(EErrorCode.CLAIM_NAME_GREATER_THAN_MAX_ALLOWED.code,
                String.valueOf(MAX_SIZE_NAME)))
            .withFieldName(PROPERTY_NAME)
            .withAttempedValue(ClaimsDTO::name)
            .withCode(EErrorCode.CLAIM_NAME_GREATER_THAN_MAX_ALLOWED.code);
    }

    private void nameMustBeOnlyAlphaCharacters() {
        ruleFor(ClaimsDTO::name)
            .must(StringPredicate.isAlpha(name -> name.replace(" ", "")))
            .when(Predicate.not(nullValue()))
            .withMessage(bundleMessageService.getMessage(EErrorCode.CLAIM_NAME_MUST_CONTAIN_ONLY_ALPHA_CHARS.code))
            .withFieldName(PROPERTY_NAME)
            .withAttempedValue(ClaimsDTO::name)
            .withCode(EErrorCode.CLAIM_NAME_MUST_CONTAIN_ONLY_ALPHA_CHARS.code);
    }

    private void required(final Function<ClaimsDTO, ?> property, final EErrorCode eErrorCode, final String propertyName) {
        ruleFor(property)
            .must(Predicate.not(nullValue()))
            .withMessage(bundleMessageService.getMessage(eErrorCode.code))
            .withCode(eErrorCode.code)
            .withFieldName(propertyName);
    }

}
