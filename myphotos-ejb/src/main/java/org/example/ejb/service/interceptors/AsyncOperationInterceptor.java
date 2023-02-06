package org.example.ejb.service.interceptors;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;


import org.example.model.model.AsyncOperation;

@SuppressWarnings("unchecked")
@Interceptor
public class AsyncOperationInterceptor {

    @AroundInvoke
    public Object aroundProcessImageResource(InvocationContext invocationContext) throws Exception {
        replaceAsyncOperationByProxy(invocationContext);
        return invocationContext.proceed();
    }

    private void replaceAsyncOperationByProxy(InvocationContext invocationContext) {
        Object[] params = invocationContext.getParameters();
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof AsyncOperation) {
                params[i] = new AsyncOperationProxy((AsyncOperation) params[i]);
            }
        }
        invocationContext.setParameters(params);
    }

    private static class AsyncOperationProxy implements AsyncOperation {

        private final AsyncOperation originalAsyncOperation;

        public AsyncOperationProxy(AsyncOperation originalAsyncOperation) {
            this.originalAsyncOperation = originalAsyncOperation;
        }

        @Override
        public long getTimeOutOfOperationInMillis() {
            return originalAsyncOperation.getTimeOutOfOperationInMillis();
        }

        @Override
        public void onSuccessOperation(Object result) {
            originalAsyncOperation.onSuccessOperation(result);
        }

        @Override
        public void onFailedOperation(Throwable throwable) {
            try {
                originalAsyncOperation.onFailedOperation(throwable);
            } catch (RuntimeException runtimeException) {
                Logger.getLogger(getClass().getName()).log(
                        Level.SEVERE,
                        "AsyncOperation.onFailedOperation throws exception: " + runtimeException.getMessage(),
                        runtimeException);
            }
        }
    }
}