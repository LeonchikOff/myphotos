package org.example.rest.error_mappers;

import org.example.model.exception.InvalidAccessTokenException;
import org.example.rest.Constants;
import org.example.rest.StatusMessage;
import org.example.rest.model.ErrorMessageRest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class InvalidAccessTokenExceptionMapper implements ExceptionMapper<InvalidAccessTokenException> {
    @Override
    public Response toResponse(InvalidAccessTokenException exception) {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorMessageRest(StatusMessage.INVALID_ACCESS_TOKEN))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
