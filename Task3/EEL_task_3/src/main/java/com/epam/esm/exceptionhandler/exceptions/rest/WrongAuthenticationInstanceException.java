package com.epam.esm.exceptionhandler.exceptions.rest;

public class WrongAuthenticationInstanceException extends RuntimeException {
    public WrongAuthenticationInstanceException(String e) {
        super(e);
    }
}
