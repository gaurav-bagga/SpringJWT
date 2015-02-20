package com.green.jwt.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.green.jwt.service.LoginService;
import com.green.jwt.service.TokenService;


@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

	@Mock private TokenService tokenService;
	@InjectMocks private LoginService loginService;
	
	@Test
	public void test(){
		System.out.println("tokenService " + tokenService);
		System.out.println("loginService " + loginService);
	}
	
	
}
