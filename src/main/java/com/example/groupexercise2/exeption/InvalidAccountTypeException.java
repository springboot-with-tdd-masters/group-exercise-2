package com.example.groupexercise2.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidAccountTypeException extends RuntimeException {

	public InvalidAccountTypeException() {
		super("Invalid Account Type");
	}
}
