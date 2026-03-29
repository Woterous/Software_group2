package com.group02.tars.service;

public class ServiceException extends Exception {

    private final int httpStatus;
    private final String code;

    public ServiceException(int httpStatus, String code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public int httpStatus() {
        return httpStatus;
    }

    public String code() {
        return code;
    }
}
