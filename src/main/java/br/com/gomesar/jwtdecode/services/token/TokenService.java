package br.com.gomesar.jwtdecode.services.token;

import br.com.gomesar.jwtdecode.commons.exceptions.BusinessLogicException;
import br.com.gomesar.jwtdecode.services.jwt.IJwtService;
import br.com.gomesar.jwtdecode.services.validators.ClaimDTOValidator;
import org.springframework.stereotype.Service;

@Service
public class TokenService implements ITokenService {

    private final IJwtService jwtService;
    private final ClaimDTOValidator claimDTOValidator;

    public TokenService(final IJwtService jwtService,
                        final ClaimDTOValidator claimDTOValidator
    ) {
        this.jwtService = jwtService;
        this.claimDTOValidator = claimDTOValidator;
    }

    @Override
    public boolean decodeToken(final String jwt) {
        final var claimDTO = jwtService.decode(jwt);
        claimDTOValidator.validate(claimDTO).isInvalidThrow(BusinessLogicException.class);
        return true;
    }
}
