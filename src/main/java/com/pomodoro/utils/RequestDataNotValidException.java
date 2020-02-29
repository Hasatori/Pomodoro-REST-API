package com.pomodoro.utils;

import java.util.List;

public class RequestDataNotValidException extends Exception {

    private final List<RequestError> errorList;

    public RequestDataNotValidException(List<RequestError> errorList){
        this.errorList=errorList;
    }

    public List<RequestError> getErrorList() {
        return errorList;
    }
}
