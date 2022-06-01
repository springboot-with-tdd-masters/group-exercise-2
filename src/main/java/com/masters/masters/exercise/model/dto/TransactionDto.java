/**
 * 
 */
package com.masters.masters.exercise.model.dto;

/**
 * @author michaeldelacruz
 *
 */
public class TransactionDto {

	private Long id;

	private String type;
	
	private double amount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
