package com.epam.esm.exceptionhandler.exceptions.nonrest;

public class KeyPairNotFoundException extends RuntimeException {
    public KeyPairNotFoundException(String e) {
        super(e);
    }
}
