package org.example.rest.error_mappers;

import org.example.model.exception.ValidationException;
import org.example.rest.model.ErrorMessageRest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    @Override
    public Response toResponse(ValidationException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessageRest(exception.getMessage(), true))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
