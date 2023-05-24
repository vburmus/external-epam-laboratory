package com.epam.esm.exceptionhandler.exceptions;

public class WrongAuthenticationInstance extends RuntimeException {
    public WrongAuthenticationInstance(String e) {
        super(e);
    }
}
