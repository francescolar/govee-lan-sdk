package io.github.govee.lan.exception;

public class GoveeException extends RuntimeException {

    public GoveeException(String message) {
        super(message);
    }

    public GoveeException(String message, Throwable cause) {
        super(message, cause);
    }
}
