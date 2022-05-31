package com.example.groupexercise2.model.dto;

import java.util.Objects;

public class TransactionRequestDto {

	private String type;
	private Double amount;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TransactionRequestDto that = (TransactionRequestDto) o;
		return Objects.equals(type, that.type) && Objects.equals(amount, that.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, amount);
	}
}
