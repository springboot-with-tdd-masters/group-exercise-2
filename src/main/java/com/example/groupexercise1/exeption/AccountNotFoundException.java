package com.example.groupexercise1.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2433483504677527984L;

	public AccountNotFoundException() {
		super("Account Not Found");
	}
	
	public AccountNotFoundException(String message) {
		super(message);
	}
}
