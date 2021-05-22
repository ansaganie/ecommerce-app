package com.ansagan.ecommerceapp.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CreateUserRequest {

	@JsonProperty
	private String username;
	private String password;
	private String confirmPassword;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public void setPassword(String testPassword) {
    }

	public void setConfirmPassword(String testPassword) {
	}
}
