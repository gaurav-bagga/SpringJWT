package com.green.jwt.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {

	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain filterChain;
	
	@InjectMocks private AuthenticationFilter authenticationFilter;
	
	@Test
	public void itShouldNotSetUnAuthorizedAccessStatusWhenSecurityContextContainsASubject() throws IOException, ServletException{
		try{
			//given a security context subject
			Map<String, Object> claims = new HashMap<String, Object>();
			claims.put("iss", "iss");
			claims.put("sub", "sub");
			claims.put("aud", "aud");
			
			SecurityContext.setContextSubject("token", claims);
			
			//when authentication filter is called
			authenticationFilter.doFilter(request, response, filterChain);
			
			//then it should call the next filter in chain
			verify(filterChain).doFilter(request, response);
			//and no unauthorized status is set
			verify(response,never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}finally{
			SecurityContext.clearContextSubject();
		}
	}
	
	@Test
	public void itShouldSetUnAuthorizedAccessStatusWhenSecurityContextContainsASubject() throws IOException, ServletException{
		//given no security context subject
		
		//when authentication filter is called
		authenticationFilter.doFilter(request, response, filterChain);
		
		//then it should not call the next filter in chain
		verify(filterChain,never()).doFilter(request, response);
		//and unauthorized status is set
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
