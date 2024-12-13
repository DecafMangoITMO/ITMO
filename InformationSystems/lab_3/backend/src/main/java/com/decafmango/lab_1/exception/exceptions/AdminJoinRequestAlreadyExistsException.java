package com.decafmango.lab_1.exception.exceptions;

public class AdminJoinRequestAlreadyExistsException extends RuntimeException {
    public AdminJoinRequestAlreadyExistsException(String message) {
        super(message);
    }
}
