package org.example.model;

public interface AsyncOperation<TypeOfObjectOperationIsExecuted> {

    long getTimeOutOfOperationInMillis();
    void onSuccessOperation(TypeOfObjectOperationIsExecuted result);
    void onFailedOperation(Throwable throwable);
}
