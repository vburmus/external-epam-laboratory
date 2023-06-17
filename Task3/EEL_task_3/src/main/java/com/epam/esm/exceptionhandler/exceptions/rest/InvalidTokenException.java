package com.epam.esm.exceptionhandler.exceptions.rest;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String e) {
        super(e);
    }
}
