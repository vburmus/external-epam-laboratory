package com.epam.esm.exceptionhandler.exceptions.rest;

public class InvalidTokenError extends RuntimeException {
    public InvalidTokenError(String e) {
        super(e);
    }
}
