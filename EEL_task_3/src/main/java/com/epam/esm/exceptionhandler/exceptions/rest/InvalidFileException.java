package com.epam.esm.exceptionhandler.exceptions.rest;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String e) {
        super(e);
    }
}