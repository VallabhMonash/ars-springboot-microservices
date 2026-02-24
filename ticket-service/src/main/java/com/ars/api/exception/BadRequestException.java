package com.ars.api.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
