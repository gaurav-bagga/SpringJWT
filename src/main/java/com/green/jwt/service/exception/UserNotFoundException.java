package com.green.jwt.service.exception;

/**
 * Exception to be throws when User cannot be found in the system.
 * 
 * @author gaurav.bagga
 *
 */
public class UserNotFoundException extends RuntimeException{

	public UserNotFoundException(String name) {
		super("User not found in system " + name);
	}

	private static final long serialVersionUID = 4424434712122830659L;

}
