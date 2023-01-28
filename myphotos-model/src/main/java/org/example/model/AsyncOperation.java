package org.example.model;

public interface AsyncOperation<T> {

    long getTimeOutOfOperationInMillis();
    void onSuccessOperation(T result);
    void onFailedOperation(Throwable throwable);
}
