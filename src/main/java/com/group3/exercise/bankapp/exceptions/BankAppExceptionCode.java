package com.group3.exercise.bankapp.exceptions;

import org.springframework.http.HttpStatus;

public enum BankAppExceptionCode {
    // Transaction Exceptions
    TRANSACTION_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, "Invalid Transaction Type"),
    INVALID_AMOUNT_EXCEPTION(HttpStatus.BAD_REQUEST, "Please insert a valid amount"),
    ACCOUNT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "Unable to process your request. Account does not exists"),
    TRANSACTION_LOG_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "Unable to process your request. Transaction log does not exists"),
    ACCOUNT_TYPE_EXCEPTION(HttpStatus.BAD_REQUEST, "Invalid Account Type"),
    MAPPING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to map response"),
    SERVER_TRANSACTION_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process your request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process your request"),
    // JWT Exceptions
    NO_JWT_EXCEPTION(HttpStatus.FORBIDDEN, "Unable to access resource without authorization"),
    REGISTER_EXCEPTION(HttpStatus.BAD_REQUEST, "Invalid User Format"),
    JWT_INVALID_EXCEPTION(HttpStatus.UNAUTHORIZED, "Unauthorized."),
    CREDENTIALS_INVALID_EXCEPTION(HttpStatus.FORBIDDEN, "Credential provided invalid"),
    USER_EXISTS_EXCEPTION(HttpStatus.CONFLICT, "username provided already exists")
    ;


    private final HttpStatus status;
    private final String message;


    BankAppExceptionCode(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
