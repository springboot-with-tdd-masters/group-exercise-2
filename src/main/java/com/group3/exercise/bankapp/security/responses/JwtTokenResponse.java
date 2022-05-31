package com.group3.exercise.bankapp.security.responses;


public class JwtTokenResponse {

	private final String token;

	public JwtTokenResponse(String jwtToken) {
		this.token = jwtToken;
	}

	public String getToken() {
		return token;
	}
	
}