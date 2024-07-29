package br.com.gomesar.jwtdecode.services.jwt;

import br.com.gomesar.jwtdecode.services.jwt.dto.ClaimsDTO;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeInvalidInputForClaimPropertyException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeInvalidTokenException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeJsonProcessingErrorException;
import br.com.gomesar.jwtdecode.services.jwt.exceptions.JwtDecodeUnrecognizedClaimPropertyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements IJwtService {
    private final ObjectMapper objectMapper;

    public JwtService(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ClaimsDTO decode(final String jwt) {
        try {
            final var jwtConsumer = new JwtConsumerBuilder().setSkipSignatureVerification().build();
            final var jwtClaims = jwtConsumer.processToClaims(jwt);
            return objectMapper.readValue(jwtClaims.getRawJson(), ClaimsDTO.class);
        } catch (InvalidJwtException exc) {
            throw new JwtDecodeInvalidTokenException(exc);
        } catch (UnrecognizedPropertyException exc) {
            throw new JwtDecodeUnrecognizedClaimPropertyException(exc);
        } catch (MismatchedInputException exc) {
            throw new JwtDecodeInvalidInputForClaimPropertyException(exc);
        } catch (JsonProcessingException exc) {
            throw new JwtDecodeJsonProcessingErrorException(exc);
        }
    }
}
