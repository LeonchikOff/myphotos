package org.example.rest.error_mappers;

import org.example.model.exception.ObjectNotFoundException;
import org.example.rest.model.ErrorMessageRest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ObjectNotFoundExceptionMapper implements ExceptionMapper<ObjectNotFoundException> {
    @Override
    public Response toResponse(ObjectNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessageRest(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
