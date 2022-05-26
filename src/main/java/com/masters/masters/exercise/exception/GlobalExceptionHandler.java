/**
 * 
 */
package com.masters.masters.exercise.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author michaeldelacruz
 *
 */

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
		return new ResponseEntity<Object>(exception.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(RecordNotFoundException.class)
	public final ResponseEntity<Object> handleAccountNotFoundException(RecordNotFoundException exception, WebRequest request) {
		return new ResponseEntity<Object>(exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(InvalidTypeException.class)
	public final ResponseEntity<Object> handleInvalidTypeException(InvalidTypeException exception, WebRequest request) {
		return new ResponseEntity<Object>(exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(AccountExistException.class)
	public final ResponseEntity<Object> handleAccountExistException(AccountExistException exception, WebRequest request) {
		return new ResponseEntity<Object>(exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(AmountExceededException.class)
	public final ResponseEntity<Object> handleAmountExceededException(AmountExceededException exception, WebRequest request) {
		return new ResponseEntity<Object>(exception.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
}
