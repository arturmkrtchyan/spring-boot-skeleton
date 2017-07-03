package io.polyglotapps.system.security;

public class InvalidJwtException extends RuntimeException {

    public InvalidJwtException(final String message, final Exception exception) {
        super(message, exception);
    }

}
