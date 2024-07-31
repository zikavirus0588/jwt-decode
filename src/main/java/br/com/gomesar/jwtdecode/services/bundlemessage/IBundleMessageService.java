package br.com.gomesar.jwtdecode.services.bundlemessage;

public interface IBundleMessageService {
    String getMessage(final String errorCode, final String... values);
}
