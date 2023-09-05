package com.vn.whatsapp.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobleException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> UserExceptionHander(UserException e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorDetail> MessageExceptionHander(MessageException e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorDetail> ChatExceptionHander(ChatException e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> MethodArgumentNotValidExceptionHander(MethodArgumentNotValidException me, WebRequest request) {
        String error = me.getBindingResult().getFieldError().getDefaultMessage();

        ErrorDetail errorDetail = new ErrorDetail("Validation Error", error, LocalDateTime.now());

        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetail> handleNoHanderFoundException(NoHandlerFoundException ex, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail("Endpoint not found", ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> otherExceptionHander(Exception e, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(e.getMessage(), request.getDescription(false), LocalDateTime.now());

        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }


}
