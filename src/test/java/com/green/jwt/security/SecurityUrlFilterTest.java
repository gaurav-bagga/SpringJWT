package com.green.jwt.security;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * This class is responsible for testing {@link SecurityUrlFilter}
 * 
 * @author gaurav.bagga
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityUrlFilterTest {
	
	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain filterChain;
	@Mock private SecurityFilter securityFilter;
	
	@InjectMocks private SecurityUrlFilter securityUrlFilter;
	
	@Test
	public void itShouldNotCheckForAuthenticationForLoginPost() throws IOException, ServletException{
		//given a request for login and POST method
		when(request.getRequestURI()).thenReturn("/login");
		when(request.getMethod()).thenReturn("POST");
		
		//when request is made
		securityUrlFilter.doFilter(request, response, filterChain);
		
		//then it should not call authentication logic
		verify(securityFilter,never()).doFilter(request, response,filterChain);
		verify(filterChain).doFilter(request, response);
	}
	
	@Test
	public void itShouldCheckForAuthenticationForNonLoginGet() throws IOException, ServletException{
		//given a request not for login and GET method
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getMethod()).thenReturn("GET");
		
		//when request is made
		securityUrlFilter.doFilter(request, response, filterChain);
		
		//then it should call authentication logic
		verify(securityFilter).doFilter(request, response,filterChain);
		verify(filterChain,never()).doFilter(request, response);
	}
	
	@Test
	public void itShouldCheckForAuthenticationForNonLoginPost() throws IOException, ServletException{
		//given a request not for login and POST method
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getMethod()).thenReturn("POST");
		
		//when request is made
		securityUrlFilter.doFilter(request, response, filterChain);
		
		//then it should call authentication logic
		verify(securityFilter).doFilter(request, response,filterChain);
		verify(filterChain,never()).doFilter(request, response);
	}
	
	
}
