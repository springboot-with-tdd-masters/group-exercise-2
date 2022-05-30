package com.example.groupexercise2.model;

import javax.persistence.Entity;

@Entity
public class RegularAccount extends Account {

	@Override
	public String getType() {
		return "regular";
	}
}
