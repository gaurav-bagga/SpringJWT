package com.green.jwt.service.exception;

public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException(String name) {
		super("User not found in system " + name);
	}

	private static final long serialVersionUID = 4424434712122830659L;

}
