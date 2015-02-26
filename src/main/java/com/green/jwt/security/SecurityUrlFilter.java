package com.green.jwt.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SecurityUrlFilter implements Filter{
	
	private SecurityFilter securityFilter;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.securityFilter = new SecurityFilter();
		this.securityFilter.init(filterConfig);
	}

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

	private boolean loginURL(HttpServletRequest httpServletRequest) {
		return httpServletRequest.getRequestURI().equals("/login") && "POST".equalsIgnoreCase(httpServletRequest.getMethod());
	}

	@Override
	public void destroy() {
		
	}

}
