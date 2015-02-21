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


/**
 * 
 * @author gaurav.bagga
 *
 */
public class SecurityFilter implements Filter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
	
	private TokenService tokenService;
	
	private AuthenticationFilter authenticationFilter;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		tokenService = WebApplicationContextUtils.
		  getRequiredWebApplicationContext(filterConfig.getServletContext()).
		  getBean(TokenService.class);
		
		authenticationFilter = new AuthenticationFilter();
		authenticationFilter.init(filterConfig);
	}

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
			authenticationFilter.doFilter(request, response, chain);
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

	private void analyseToken(String token) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, SignatureException, IOException, JWTVerifyException{
		Map<String, Object> claims = tokenService.verify(token);
		SecurityContext.setContextSubject(token,claims);
	}
}
