package com.group3.exercise.bankapp.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CheckingAccount.TYPE)
public class CheckingAccount extends Account{
	
	public static final String TYPE = "checking";
	
	@Override
	public String getType() {
		return TYPE;
	}

}
