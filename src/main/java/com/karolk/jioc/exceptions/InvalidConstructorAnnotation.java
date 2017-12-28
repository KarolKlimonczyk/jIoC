package com.karolk.jioc.exceptions;

public class InvalidConstructorAnnotation extends RuntimeException {

    public InvalidConstructorAnnotation(String message) {
        super(message);
    }
}
