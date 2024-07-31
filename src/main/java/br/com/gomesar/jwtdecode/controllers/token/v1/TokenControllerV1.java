package br.com.gomesar.jwtdecode.controllers.token.v1;

import br.com.gomesar.jwtdecode.commons.models.AbstractApiResponse;
import br.com.gomesar.jwtdecode.controllers.token.v1.models.DecodeTokenResponse;
import br.com.gomesar.jwtdecode.services.token.ITokenService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tokens")
@Validated
public class TokenControllerV1 implements ITokenControllerV1 {

    public static final String BEARER = "Bearer ";
    private final ITokenService tokenService;

    public TokenControllerV1(final ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public AbstractApiResponse<DecodeTokenResponse> decodeToken(final String headerValue) {
        return new AbstractApiResponse.Builder<DecodeTokenResponse>().
            data(new DecodeTokenResponse.Builder()
                .isValidToken(tokenService.decodeToken(headerValue.replace(BEARER, "")))
                .build())
            .build();
    }
}
