package com.karolk.jioc.exceptions;

public class SuitableConstructorNotFound extends RuntimeException {

    public SuitableConstructorNotFound(String message) {
        super(message);
    }
}
