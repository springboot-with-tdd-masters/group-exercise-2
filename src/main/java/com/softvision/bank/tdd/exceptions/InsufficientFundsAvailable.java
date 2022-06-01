package com.softvision.bank.tdd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InsufficientFundsAvailable extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InsufficientFundsAvailable() {
        super("Sorry, You have insufficient funds available.");
    }
}
