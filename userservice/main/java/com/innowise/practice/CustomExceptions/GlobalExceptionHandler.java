package com.innowise.practice.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchResourceNotFoundException(ResourceNotFoundException e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> catchDuplicateException(DuplicateException e){
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
    }
}
