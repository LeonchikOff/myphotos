package org.example.rest.error_mappers;

import org.example.model.exception.FailedRetrievalSocialDataException;
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
public class FailedRetrievalSocialDataExceptionMapper implements ExceptionMapper<FailedRetrievalSocialDataException> {

    @Inject
    private Logger logger;

    @Override
    public Response toResponse(FailedRetrievalSocialDataException exception) {
        logger.log(Level.SEVERE, StatusMessage.INTERNAL_ERROR, exception);


        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorMessageRest(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
