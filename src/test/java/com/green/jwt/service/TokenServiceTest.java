package com.green.jwt.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.auth0.jwt.JWTVerifyException;
import com.green.jwt.model.Role;
import com.green.jwt.model.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-config.xml"})
public class TokenServiceTest {

	@Autowired private TokenService tokenService;
	
	@Test(expected=IllegalArgumentException.class)
	public void itShouldNotCreateATokenForNullSubject(){
		//given a null subject
		User subject  = null;
		
		//when token is generated
		tokenService.createToken(subject);
		
		//then it should throw an exception
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void itShouldNotCreateATokenForEmptyNameSubject(){
		//given an empty subject
		User subject  = new User("        ","",Role.USER);
		
		//when token is generated
		tokenService.createToken(subject);
		
		//then it should throw an exception
	}
	
	@Test
	public void itShouldCreateATokenForAGivenValidNonEmptySubject()
			throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException,
			JWTVerifyException {
		//given non empty valid subject
		User subject  = new User("James","pass",Role.USER);
		
		//when token is generated
		String token = tokenService.createToken(subject);
		
		//then it should generate a token
		Assert.assertNotNull(token);
	}
	
	
	@Test
	public void itShouldDecryptAValidTokenAndReturnClaimsAssociatedWithTheToken()
			throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException,
			JWTVerifyException {
		//given non empty valid subject and its token
		User subject  = new User("James","pass",Role.USER);
		String token = tokenService.createToken(subject);
		
		//when token is verified
		Map<String, Object> claims = tokenService.verify(token);
		
		//then it should decrypt the token and return claims in a map
		Assert.assertEquals("James", claims.get("sub").toString());
		Assert.assertEquals("green", claims.get("iss").toString());
		Assert.assertEquals("green.com", claims.get("aud").toString());
		Assert.assertEquals(Role.USER.name(), claims.get("role"));
		Assert.assertNotNull(claims.get("jti"));
		Assert.assertNotNull(claims.get("exp"));
		Assert.assertNotNull(claims.get("nbf"));
	}
	
	
	@Test(expected=Exception.class)
	public void itShouldNotDecryptAnInvalidTokenAndThrowException()
			throws InvalidKeyException, NoSuchAlgorithmException,
			IllegalStateException, SignatureException, IOException,
			JWTVerifyException {
		//given an invalid token
		String token = "a.b.c";
		
		//when token is verified
		tokenService.verify(token);
		
		//then it should throw an exception
		
		
	}
	
	
}
