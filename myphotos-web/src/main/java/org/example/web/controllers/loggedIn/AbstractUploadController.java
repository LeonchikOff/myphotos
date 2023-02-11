package org.example.web.controllers.loggedIn;

import org.example.common.config.Constants;
import org.example.model.model.AsyncOperation;
import org.example.model.model.domain.Profile;
import org.example.web.models.PartImageResource;
import org.example.web.security.SecurityUtils;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractUploadController<TypeUploadedContent> extends HttpServlet {

    @Inject
    protected Logger logger;

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part reqPart = req.getPart("qqfile");
        Profile currentProfile = SecurityUtils.getCurrentProfile();
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(Constants.DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS);
        uploadImage(currentProfile, new PartImageResource(reqPart), new AsyncOperation<TypeUploadedContent>() {
            @Override
            public long getTimeOutOfOperationInMillis() {
                return asyncContext.getTimeout();
            }

            @Override
            public void onSuccessOperation(TypeUploadedContent result) {
                handleSuccess(asyncContext, result);
            }

            @Override
            public void onFailedOperation(Throwable throwable) {
                handleFailed(throwable, asyncContext);
            }
        });
    }

    protected abstract void uploadImage(Profile profile, PartImageResource partImageResource, AsyncOperation<TypeUploadedContent> asyncOperation);

    private void handleFailed(Throwable throwable, AsyncContext asyncContext) {
        try {
            logger.log(Level.SEVERE, "Async operation failed: " + throwable.getMessage(), throwable);
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder().add("success", false);
            JsonObject jsonObject = jsonObjectBuilder.build();
            sendJsonResponse(jsonObject, asyncContext);
        } finally {
            asyncContext.complete();
        }
    }

    private void handleSuccess(AsyncContext asyncContext, TypeUploadedContent result) {
        try {
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder().add("success", true);
            Map<String, String> resultMap = getResultMap(result, ((HttpServletRequest) asyncContext.getRequest()));
            resultMap.forEach(jsonObjectBuilder::add);
            JsonObject jsonObject = jsonObjectBuilder.build();
            sendJsonResponse(jsonObject, asyncContext);
        } finally {
            asyncContext.complete();
        }
    }

    protected abstract Map<String, String> getResultMap(TypeUploadedContent result, HttpServletRequest request);

    private void sendJsonResponse(JsonObject jsonObject, AsyncContext asyncContext) {
        try {
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            String jsonContent = jsonObject.toString();
            response.setContentLength(jsonContent.getBytes(StandardCharsets.UTF_8).length);
            response.setContentType("text/plain");
            try (Writer responseWriter = response.getWriter()) {
                responseWriter.write(jsonContent);
                responseWriter.flush();
            }
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "sendJsonResponse failed: " + ioException.getMessage(), ioException);
        }
    }

}
