package org.example.web.component;

import org.example.web.models.ErrorModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.example.web.Constants.DEFAULT_ERROR_MESSAGE;
import static org.example.web.Constants.EMPTY_MESSAGE;

@ApplicationScoped
public class ExceptionConverter {

    @Inject
    Map<Class, Integer> classExAndStatusCodeMap;

    @Inject
    Map<Integer, String> statusCodeAndMessageMap;

    public ErrorModel convertExceptionToHttpStatus(Throwable throwable) {
        if (throwable instanceof HttpStatusException) {
            HttpStatusException httpStatusException = (HttpStatusException) throwable;
            return createErrorModel(httpStatusException.getStatus(), httpStatusException.getMessage());
        } else {
            Integer statusCode = classExAndStatusCodeMap.getOrDefault(throwable.getClass(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return createErrorModel(statusCode, throwable.getMessage());
        }
    }

    private ErrorModel createErrorModel(Integer statusCode, String message) {
        if (statusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            return new ErrorModel(statusCode, DEFAULT_ERROR_MESSAGE);
        } else if (message.equals(EMPTY_MESSAGE)) {
            return new ErrorModel(statusCode, statusCodeAndMessageMap.getOrDefault(statusCode, DEFAULT_ERROR_MESSAGE));
        } else {
            return new ErrorModel(statusCode, message);
        }
    }
}
