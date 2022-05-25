package com.example.groupexercise1.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<String> handleAllException(Exception e, WebRequest req) {
		return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccountNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public final ResponseEntity<String> handleAccountNotFoundException(Exception e, WebRequest req) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidAccountTypeException.class)
	public final ResponseEntity<String> handleInvalidAccountTypeException(Exception e, WebRequest req) {
		return new ResponseEntity<String>("Invalid Account Type", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public final ResponseEntity<String> handleInsufficientBalanceException(Exception e, WebRequest req) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidTransactionAmountException.class)
	public final ResponseEntity<String> handleInvalidTransactionAmountException(Exception e, WebRequest req) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidTransactionTypeException.class)
	public final ResponseEntity<String> handleInvalidTransactionTypeException(Exception e, WebRequest req) {
		return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
