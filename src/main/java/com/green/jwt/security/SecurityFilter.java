package com.green.jwt.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.auth0.jwt.JWTVerifyException;
import com.green.jwt.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * Series of filter at position 2 first is {@link SecurityUrlFilter} then is {@link SecurityFilter}
 * {@link SecurityUrlFilter} blocks all the requests except for login,
 * {@link SecurityFilter} checks for presence of JWT token extracts claims from it and sets up context available as thread local
 * 
 * @author gaurav.bagga
 *
 */
public class SecurityFilter implements Filter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
	
    @Autowired private TokenService tokenService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContextUtils.
		  getRequiredWebApplicationContext(filterConfig.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
		
	}

	/**
	 * Checks for the presence of JWT token, if present and valid it allows to proceed.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		try{
			
			String token = httpServletRequest.getHeader(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_HEADER);
			if(token == null){
				httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			analyseToken(token);
			chain.doFilter(request, response);
		}catch(Exception e){
			LOGGER.error("Problem in analysing token.",e);
			httpServletResponse.setHeader(SecurityConstant.JWT_SECURTY_TOKEN_HTTP_ERROR_HEADER,"Error in processing : " + e.getMessage());
			httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}finally{
			SecurityContext.clearContextSubject();
		}
		
	}

	@Override
	public void destroy() {}

	/**
	 * Verifies the token and puts the claims map into thread local SecurityContext.
	 * 
	 * @param token
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws JWTVerifyException
	 */
	private void analyseToken(String token) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, SignatureException, IOException, JWTVerifyException{
		Map<String, Object> claims = tokenService.verify(token);
		SecurityContext.setContextSubject(token,claims);
	}
}
