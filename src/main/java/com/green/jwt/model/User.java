package com.green.jwt.model;

public class User {
	
	public final String name;
	public final String password;
	public final Role role;
	
	public User(String name, String password,Role role){
		this.name = name;
		this.password = password;
		this.role = role;
	}

}
