package com.epam.esm.exceptionhandler.exceptions.rest;

public class KeyPairNotFoundException extends RuntimeException {
    public KeyPairNotFoundException(String e) {
        super(e);
    }
}
