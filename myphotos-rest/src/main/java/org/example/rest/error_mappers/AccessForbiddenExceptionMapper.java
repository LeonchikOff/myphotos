package org.example.rest.error_mappers;

import org.example.model.exception.AccessForbiddenException;
import org.example.rest.model.ErrorMessageRest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class AccessForbiddenExceptionMapper implements ExceptionMapper<AccessForbiddenException> {
    @Override
    public Response toResponse(AccessForbiddenException exception) {
        return Response
                .status(Response.Status.FORBIDDEN)
                .entity(new ErrorMessageRest(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
