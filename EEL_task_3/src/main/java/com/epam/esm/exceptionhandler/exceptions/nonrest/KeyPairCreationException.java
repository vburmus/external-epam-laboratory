package com.epam.esm.exceptionhandler.exceptions.nonrest;

public class KeyPairCreationException extends RuntimeException {
    public KeyPairCreationException(String e) {
        super(e);
    }
}