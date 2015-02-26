package com.green.jwt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
	public final String name;
	public final String password;
	public final Role role;
	
    @JsonCreator
	public User(@JsonProperty("name") String name,
			@JsonProperty("password") String password,
			@JsonProperty("role") Role role){
		this.name = name;
		this.password = password;
		this.role = role;
	}
        
        

}
