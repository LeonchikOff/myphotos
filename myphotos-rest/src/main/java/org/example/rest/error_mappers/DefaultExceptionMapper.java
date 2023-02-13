package org.example.rest.error_mappers;

import org.example.rest.StatusMessage;
import org.example.rest.model.ErrorMessageRest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
@ApplicationScoped
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

    @Inject
    private Logger logger;

    @Override
    public Response toResponse(Throwable exception) {
        logger.log(Level.SEVERE, StatusMessage.INTERNAL_ERROR, exception);
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorMessageRest(StatusMessage.INTERNAL_ERROR))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
