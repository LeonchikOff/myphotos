package org.example.ws.error;

import org.example.model.exception.BusinessException;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
public class ExceptionMapperInterceptor {

    @Inject
    Logger logger;


    @AroundInvoke
    public Object aroundProcessImageResource(InvocationContext invocationContext) throws SOAPException {
        Object[] invocationContextParameters = invocationContext.getParameters();
        for (int i = 0; i < invocationContextParameters.length; i++)
            if (invocationContextParameters[i] == null)
                throw new SOAPFaultException(createSoapFault("Parameter [" + i + "] is invalid", true));
        try {
            return invocationContext.proceed();
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                logger.log(Level.WARNING, "WS request failed: {0}", e.getMessage());
                throw new SOAPFaultException(createSoapFault(e.getMessage(), true));
            } else {
                logger.log(Level.SEVERE, "WS request failed: {0}", e.getMessage());
                throw new SOAPFaultException(createSoapFault("Internal error", false));
            }
        }
    }

    private SOAPFault createSoapFault(String message, boolean isClientError) throws SOAPException {
        String errorType = isClientError ? "Client" : "Server";
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        return soapFactory.createFault(
                message,
                new QName("http://schemas.xmlsoap.org/soap/envelope/", errorType, ""));
    }
}
