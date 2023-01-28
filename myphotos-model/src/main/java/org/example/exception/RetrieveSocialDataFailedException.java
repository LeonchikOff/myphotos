package org.example.exception;

public class RetrieveSocialDataFailedException extends ApplicationException {
    public RetrieveSocialDataFailedException(String message) {
        super(message);
    }

    public RetrieveSocialDataFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
