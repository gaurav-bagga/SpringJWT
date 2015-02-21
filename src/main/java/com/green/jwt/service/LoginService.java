package com.green.jwt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.green.jwt.model.Role;
import com.green.jwt.model.User;
import com.green.jwt.service.exception.UserAuthenticationException;
import com.green.jwt.service.exception.UserNotFoundException;

@Service
public class LoginService {

	public static final Map<String, User> USER_DB = new HashMap<String, User>();
	
	@Autowired private TokenService tokenService;
	
	static {
		USER_DB.put("james", new User("james","pass", Role.USER));
		USER_DB.put("steve", new User("steve", "pass", Role.ADMIN));
	}
	
	public String login(User user){
		User userFromDb = USER_DB.get(user.name);
		if(userFromDb == null){
			throw new UserNotFoundException(user.name);
		}
		
		if(!userFromDb.password.equals(user.password)){
			throw new UserAuthenticationException();
		}
		
		return tokenService.createToken(user);
	}
	
}
