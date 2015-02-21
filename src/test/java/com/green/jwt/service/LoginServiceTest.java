package com.green.jwt.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import com.green.jwt.model.Role;
import com.green.jwt.model.User;
import com.green.jwt.service.LoginService;
import com.green.jwt.service.TokenService;
import com.green.jwt.service.exception.UserAuthenticationException;
import com.green.jwt.service.exception.UserNotFoundException;


@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

	@Mock private TokenService tokenService;
	@InjectMocks private LoginService loginService;
	
	@Test(expected=UserNotFoundException.class)
	public void itShouldThrowExceptionForUserNotFoundInSystem(){
		//given a user not present in database
		User user = new User("Anon","pass",Role.USER);
		
		//when he tries to login
		loginService.login(user);
		
		//then UserNotFoundException exception should be thrown
	}
	
	
	@Test(expected=UserAuthenticationException.class)
	public void itShouldThrowExceptionOnAuthenticationFailure(){
		//given a user present in database, but wrong password.
		User user = new User("james","wrong", Role.USER);
		
		//when he tries to login
		loginService.login(user);
		
		//then UserAuthenticationException exception should be thrown
	}
	
	
	@Test 
	public void itShouldReturnValidJWTTokenOnSuccessfullAuthentcation(){
		//given a valid user
		User user = LoginService.USER_DB.get("james");
		when(tokenService.createToken(user)).thenReturn("valid_token");
		
		//when he tries to login
		String token = loginService.login(user);
		
		//then valid token should be returned
		verify(tokenService).createToken(user);
		Assert.assertEquals("valid_token", token);
		
	}
	
	
}
