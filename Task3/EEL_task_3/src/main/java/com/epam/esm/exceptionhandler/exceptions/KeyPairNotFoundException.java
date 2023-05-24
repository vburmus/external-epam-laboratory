package com.epam.esm.exceptionhandler.exceptions;

public class KeyPairNotFoundException extends RuntimeException {
    public KeyPairNotFoundException(String e) {
        super(e);
    }
}
