package org.example.web.filters;

import org.example.model.exception.BusinessException;
import org.example.web.Constants;
import org.example.web.component.ExceptionConverter;
import org.example.web.component.HttpStatusException;
import org.example.web.models.ErrorModel;
import org.example.web.utils.WebUtils;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(filterName = "ErrorFilter", asyncSupported = true)
public class ErrorFilter implements Filter {
    @Inject
    private Logger logger;

    @Inject
    private ExceptionConverter exceptionConverter;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            filterChain.doFilter(request,
                    new ThrowExceptionInsteadOfSendErrorResponse(response));
        } catch (Throwable th) {
            Throwable throwable = unWrapThrowable(th);
            logError(request, throwable);
            ErrorModel errorModel = exceptionConverter.convertExceptionToHttpStatus(throwable);
            handleError(request, errorModel, response);
        }
    }

    private static class ThrowExceptionInsteadOfSendErrorResponse extends HttpServletResponseWrapper {

        public ThrowExceptionInsteadOfSendErrorResponse(HttpServletResponse response) {
            super(response);
        }
        @Override
        public void sendError(int sc, String msg) throws IOException {
            throw new HttpStatusException(sc, msg);
        }

        @Override
        public void sendError(int sc) throws IOException {
            super.sendError(sc, Constants.EMPTY_MESSAGE);
        }

    }
    private void handleError(HttpServletRequest request, ErrorModel errorModel, HttpServletResponse response) throws IOException, ServletException {
        response.reset();
        response.setStatus(errorModel.getStatus());
        if (WebUtils.isAjaxRequest(request)) {
            sendAjaxJsonErrorResponse(errorModel, request, response);
        } else {
            sendErrorPage(errorModel, request, response);
        }
    }

    private void sendErrorPage(ErrorModel errorModel, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("errorModel", errorModel);

        request.setAttribute("dynamicPage", "../view/error.jsp");
        request.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(request, response);
    }

    private void sendAjaxJsonErrorResponse(ErrorModel errorModel, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObjectError = Json.createObjectBuilder()
                .add("success", false)
                .add("error", errorModel.getMessage())
                .build();
        String jsonContext = jsonObjectError.toString();
        int lengthContent = jsonContext.getBytes(StandardCharsets.UTF_8).length;
        response.setContentLength(lengthContent);
        response.setContentType("application/json");
        try (Writer responseWriter = response.getWriter()) {
            responseWriter.write(jsonContext);
            responseWriter.flush();
        }
    }

    private Throwable unWrapThrowable(Throwable throwable) {
        return throwable instanceof ServletException
                && throwable.getCause() != null
                ? throwable.getCause()
                : throwable;
    }

    private void logError(HttpServletRequest request, Throwable throwable) {
        String errorMsg = String.format("Can't process request: %s -> %s", request.getRequestURI(), throwable.getMessage());
        if (throwable instanceof BusinessException) {
            logger.log(Level.WARNING, errorMsg);
        } else {
            logger.log(Level.SEVERE, errorMsg, throwable);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {

    }
}
