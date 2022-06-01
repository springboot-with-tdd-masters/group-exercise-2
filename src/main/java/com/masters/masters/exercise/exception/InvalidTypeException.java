/**
 * 
 */
package com.masters.masters.exercise.exception;

/**
 * @author michaeldelacruz
 *
 */


public class InvalidTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTypeException(String message){
        super(message);
    }

    public InvalidTypeException(String message, Throwable cause){
        super(message,cause);
    }
	
}
