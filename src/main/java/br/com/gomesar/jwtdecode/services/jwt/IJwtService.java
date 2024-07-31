package br.com.gomesar.jwtdecode.services.jwt;

import br.com.gomesar.jwtdecode.services.jwt.dto.ClaimsDTO;

public interface IJwtService {
    ClaimsDTO decode(final String jwt);
}
