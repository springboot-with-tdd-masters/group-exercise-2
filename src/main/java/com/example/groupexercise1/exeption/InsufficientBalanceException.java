package com.example.groupexercise1.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends RuntimeException {

	private static final long serialVersionUID = -2433483504677527984L;

	public InsufficientBalanceException() {
		super("Insufficient Balance");
	}
	
	public InsufficientBalanceException(String message) {
		super(message);
	}
}
