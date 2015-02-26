package com.green.jwt.controllers;

import com.green.jwt.model.User;
import com.green.jwt.service.LoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Login controller responsible for JWT tokens
 * 
 * @author gaurav.bagga
 *
 */
@Controller
public class LoginController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;

	/**
	 * If credentials are correct returns a JWT token
	 * 
	 * @param user
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return JWT token if credentials are valid else {@link HttpServletResponse.SC_UNAUTHORIZED} status code
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String genToken(@RequestBody User user,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		try {
			return loginService.login(user);
		} catch (Exception e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			LOGGER.error("Problem in login controller.", e);
			return "";

		}
	}
}
