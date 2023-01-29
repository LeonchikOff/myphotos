package org.example.exception;

public class FailedRetrievalSocialDataException extends ApplicationException {
    public FailedRetrievalSocialDataException(String message) {
        super(message);
    }

    public FailedRetrievalSocialDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
