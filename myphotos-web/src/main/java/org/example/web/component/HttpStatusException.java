package org.example.web.component;

import org.example.model.exception.ApplicationException;

public class HttpStatusException extends ApplicationException {
    private final int status;

    public HttpStatusException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
