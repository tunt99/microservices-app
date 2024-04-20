package com.microservices.app.exception;

public class BaseResponseException extends RuntimeException {

    public BaseResponseException() {
        super();
    }

    public BaseResponseException(String message) {
        super(message);
    }

    public BaseResponseException(String message, Throwable t) {
        super(message, t);
    }
}
