/**
 * 
 */
package com.masters.masters.exercise.exception;

/**
 * @author michaeldelacruz
 *
 */

public class AccountExistException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountExistException(String message){
        super(message);
    }

    public AccountExistException(String message, Throwable cause){
        super(message,cause);
    }
}
