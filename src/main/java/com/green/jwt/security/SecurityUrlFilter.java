package com.green.jwt.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Series of filter at position 2 first is {@link SecurityUrlFilter} then is {@link SecurityFilter}
 * {@link SecurityUrlFilter} blocks all the requests except for login,
 * {@link SecurityFilter} checks for presence of JWT token extracts claims from it and sets up context available as thread local
 * 
 * @author gaurav.bagga
 *
 */
public class SecurityUrlFilter implements Filter{
	
	private SecurityFilter securityFilter;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.securityFilter = new SecurityFilter();
		this.securityFilter.init(filterConfig);
	}

	/**
	 * Checks request is for login and method is POST, if yes it does not checks for JWT token.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		if(!loginURL(httpServletRequest)){
			securityFilter.doFilter(request, response, chain);
            return;
		}
		chain.doFilter(request, response);
		
	}

	/**
	 * Url is for login and method is POST
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	private boolean loginURL(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getRequestURI().contains("/login") && "POST".equalsIgnoreCase(httpServletRequest.getMethod());
	}

	@Override
	public void destroy() {
		
	}

}
