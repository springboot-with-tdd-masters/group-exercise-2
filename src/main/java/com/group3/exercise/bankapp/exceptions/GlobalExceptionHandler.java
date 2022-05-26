package com.group3.exercise.bankapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BankAppException.class)
    public ResponseEntity<ErrorMsgWrapper> handleBankAppException(BankAppException ex, WebRequest req){
        return ResponseEntity.status(ex.getStatus()).body(new ErrorMsgWrapper(ex.getMessage()));
    }
}
