package com.decafmango.lab_1.exception;

import com.decafmango.lab_1.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCarAlreadyExistsException(CarAlreadyExistsException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCoordinatesAlreadyExistException(CoordinatesAlreadyExistException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHumanBeingAlreadyExistsException(HumanBeingAlreadyExistsException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAdminJoinRequestAlreadyExistsException(AdminJoinRequestAlreadyExistsException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserIsAdminAlreadyException(UserIsAlreadyAdminException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCarNotFoundException(CarNotFoundException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCoordinatesNotFoundException(CoordinatesNotFoundException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHumanBeingNotFoundException(HumanBeingNotFoundException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAdminJoinRequestNotFoundException(AdminJoinRequestNotFoundException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }
}
