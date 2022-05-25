package com.example.groupexercise1.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTransactionTypeException extends RuntimeException {

	public InvalidTransactionTypeException() {
		super("Invalid Transaction Type");
	}
}
