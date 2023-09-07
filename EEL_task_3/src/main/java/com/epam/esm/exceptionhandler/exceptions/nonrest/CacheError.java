package com.epam.esm.exceptionhandler.exceptions.nonrest;

public class CacheError extends RuntimeException {
    public CacheError(String s) {
        super(s);
    }
}