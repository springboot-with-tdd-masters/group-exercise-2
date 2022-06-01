package com.softvision.bank.tdd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 5545466885256769635L;

	public BadRequestException() {
		super("No record exists for the given ID.");
	}
	
	public BadRequestException(String message) {
		super(message);
	}
	
	public BadRequestException(String message, Throwable t) {
		super(message, t);
	}
}
