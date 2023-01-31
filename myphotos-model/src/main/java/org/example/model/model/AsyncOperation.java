package org.example.model.model;

public interface AsyncOperation<TypeOfObjectOperationIsExecuted> {

    long getTimeOutOfOperationInMillis();
    void onSuccessOperation(TypeOfObjectOperationIsExecuted result);
    void onFailedOperation(Throwable throwable);
}
