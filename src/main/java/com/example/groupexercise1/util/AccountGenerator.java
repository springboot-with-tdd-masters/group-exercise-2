package com.example.groupexercise1.util;

import java.util.Date;

public final class AccountGenerator {

	private AccountGenerator() {}
	
	public static String generateAccountNumber() {
		Date date = new Date();
		return ((Long)date.getTime()).toString();
	}
}
