package com.green.jwt.security;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
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

import com.auth0.jwt.JWTVerifyException;
import com.green.jwt.service.TokenService;


/**
 * This class is responsible for testing {@link SecurityFilter}
 * 
 * @author gaurav.bagga
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityFilterTest {
	
	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain filterChain;
	@Mock private TokenService tokenService;
	
	@InjectMocks private SecurityFilter securityFilter;
	
	
	@Test
	public void itShouldReturnUnauthorizedAccessHeaderIfTokenIsMissing() throws IOException, ServletException{
		//given a request with no token
		
		//when request is made
		securityFilter.doFilter(request, response, filterChain);

		//then status should be returned with code 401 i.e. SC_UNAUTHORIZED
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(request).getHeader(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_HEADER);
		
		//and authentication filter should not be called
		verify(filterChain,never()).doFilter(request, response);
		
	}
	
	
	public void itShouldReturnInternalErrorHeaderIfInvalidTokenIsSent() throws IOException, ServletException{
		//given a request with invalid token
		when(request.getHeader(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_HEADER)).thenReturn("invalid_token");
		
		//when request is made
		securityFilter.doFilter(request, response, filterChain);
		
		//then status should be returned with code 500 i.e. SC_INTERNAL_SERVER_ERROR
		verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		verify(request).getHeader(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_HEADER);
		verify(response).setHeader(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_ERROR_HEADER, anyString());
		
		//and authentication filter should not be called
		verify(filterChain,never()).doFilter(request, response);
		
	}
	

	@Test
	public void itShouldPassCallToAuthenticationFilterAndNoUnauthorizedStatusIsSetForValidToken() throws IOException, ServletException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, SignatureException, JWTVerifyException{
		//given a request with valid token
		when(request.getHeader(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_HEADER)).thenReturn("valid_token");
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("iss", "iss");
		claims.put("sub", "sub");
		claims.put("aud", "aud");
		when(tokenService.verify("valid_token")).thenReturn(claims);
		
		//when request is made
		securityFilter.doFilter(request, response, filterChain);
		
		//then authentication filter is called
		verify(filterChain).doFilter(request, response);
		//and unauthorized status is not set
		verify(response,never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
