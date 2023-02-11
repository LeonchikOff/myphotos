package org.example.web.component;

import org.example.model.exception.AccessForbiddenException;
import org.example.model.exception.InvalidAccessTokenException;
import org.example.model.exception.ObjectNotFoundException;
import org.example.model.exception.ValidationException;
import org.example.web.Constants;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Dependent
public class ExceptionMappingProducer {

    @Produces
    public Map<Class, Integer> getExceptionToStatusCodeMapping() {
        Map<Class<? extends Throwable>, Integer> map = new HashMap<>();
        map.put(ObjectNotFoundException.class, HttpServletResponse.SC_NOT_FOUND);
        map.put(ValidationException.class, HttpServletResponse.SC_BAD_REQUEST);
        map.put(AccessForbiddenException.class, HttpServletResponse.SC_FORBIDDEN);
        map.put(InvalidAccessTokenException.class, HttpServletResponse.SC_UNAUTHORIZED);
        return Collections.unmodifiableMap(map);
    }

    @Produces
    public Map<Integer, String> getStatusCodeToMessageMapping() {
        Map<Integer, String> map = new HashMap<>();
        map.put(HttpServletResponse.SC_NOT_FOUND, "Requested resource not found");
        map.put(HttpServletResponse.SC_BAD_REQUEST, "Current request is invalid");
        map.put(HttpServletResponse.SC_FORBIDDEN, "Requested operation not permitted");
        map.put(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
        map.put(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Constants.DEFAULT_ERROR_MESSAGE);
        return map;
    }


}
