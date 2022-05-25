package com.example.groupexercise1.model;

import javax.persistence.Entity;

@Entity
public class InterestAccount extends Account {

	@Override
	public String getType() {
		return "interest";
	}
}
