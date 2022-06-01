/**
 * 
 */
package com.masters.masters.exercise.exception;

/**
 * @author michaeldelacruz
 *
 */
public class AmountExceededException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AmountExceededException(String message){
        super(message);
    }

    public AmountExceededException(String message, Throwable cause){
        super(message,cause);
    }
	
}
