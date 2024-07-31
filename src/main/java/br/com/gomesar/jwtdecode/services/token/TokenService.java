package br.com.gomesar.jwtdecode.services.token;

import br.com.gomesar.jwtdecode.commons.exceptions.BusinessLogicException;
import br.com.gomesar.jwtdecode.services.jwt.IJwtService;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeInvalidInputForClaimPropertyException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeInvalidTokenException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeJsonProcessingErrorException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeUnrecognizedClaimPropertyException;
import br.com.gomesar.jwtdecode.services.validators.ClaimDTOValidator;
import im.aop.loggers.advice.afterthrowing.LogAfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenService implements ITokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final IJwtService jwtService;
    private final ClaimDTOValidator claimDTOValidator;

    public TokenService(final IJwtService jwtService,
                        final ClaimDTOValidator claimDTOValidator
    ) {
        this.jwtService = jwtService;
        this.claimDTOValidator = claimDTOValidator;
    }

    @Override
    @LogAfterThrowing(declaringClass = TokenService.class,
        ignoreExceptions = {JwtDecodeInvalidTokenException.class, JwtDecodeUnrecognizedClaimPropertyException.class,
            JwtDecodeInvalidInputForClaimPropertyException.class, JwtDecodeJsonProcessingErrorException.class})
    public boolean decodeToken(final String jwt) {
        logger.info("iniciando serviço para decodificar o JWT");
        final var claimDTO = jwtService.decode(jwt);
        logger.info("Mapeamento finalizado. Iniciando validação do claims: {}", claimDTO);
        claimDTOValidator.validate(claimDTO).isInvalidThrow(BusinessLogicException.class);
        logger.info("claims validado com sucesso");
        return true;
    }
}
