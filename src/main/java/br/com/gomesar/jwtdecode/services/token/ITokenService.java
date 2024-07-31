package br.com.gomesar.jwtdecode.services.token;

public interface ITokenService {
    boolean decodeToken(final String jwt);
}
