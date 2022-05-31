package com.softvision.bank.tdd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserExistsException extends ForbiddenException {

	private static final long serialVersionUID = 7469339035876899219L;

	public UserExistsException() {
        super("User already exists.");
    }
    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable t) {
        super(message, t);
    }
}
