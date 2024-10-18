package com.decafmango.lab_1.exception.exceptions;

public class UserIsAlreadyAdminException extends RuntimeException {
    public UserIsAlreadyAdminException(String message) {
        super(message);
    }
}
