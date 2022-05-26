package com.group3.exercise.bankapp.exceptions;

import org.springframework.http.HttpStatus;

public class BankAppException extends RuntimeException {
    private BankAppExceptionCode code;

    public BankAppException(BankAppExceptionCode code){
        super(code.getMessage());
        this.code = code;
    }

    public HttpStatus getStatus(){
        return code.getStatus();
    }
    public String getMessage(){
        return super.getMessage();
    }
}
