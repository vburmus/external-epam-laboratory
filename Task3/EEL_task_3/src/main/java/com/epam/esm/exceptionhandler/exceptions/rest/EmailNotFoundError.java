package com.epam.esm.exceptionhandler.exceptions.rest;

public class EmailNotFoundError extends RuntimeException{
    public EmailNotFoundError(String e) {
        super(e);
    }
}
