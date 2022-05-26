/**
 * 
 */
package com.masters.masters.exercise.model.dto;

/**
 * @author michaeldelacruz
 *
 */
public class TransactionDto {

	private String type;
	
	private double amount;

	/**
	 * @return the transactionType
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the transactionType to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
