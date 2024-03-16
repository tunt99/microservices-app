package com.microservices.app.exception;

public class ElasticQueryServiceException extends RuntimeException {

    public ElasticQueryServiceException() {
        super();
    }

    public ElasticQueryServiceException(String message) {
        super(message);
    }

    public ElasticQueryServiceException(String message, Throwable t) {
        super(message, t);
    }
}
