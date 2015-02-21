package com.green.jwt.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTSigner.Options;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.green.jwt.model.User;

/**
 * 
 * @author gaurav.bagga
 * 
 *         This class is responsible for generating JWT tokens and verifying
 *         them.
 * 		
 * 		   <br />	
 *         Below are the claims possible in JWT
 * 
 *         <ul>
 * 
 *         <li>"iss" (Issuer) Claim
 * 
 *         <p>
 *         The iss (issuer) claim identifies the principal that issued the JWT.
 *         The processing of this claim is generally application specific. The
 *         iss value is a case-sensitive string containing a StringOrURI value.
 *         Use of this claim is OPTIONAL.
 *         </p>
 *         </li>
 * 
 * 
 *         <li>"sub" (Subject) Claim
 * 
 *         <p>
 *         The sub (subject) claim identifies the principal that is the subject
 *         of the JWT. The Claims in a JWT are normally statements about the
 *         subject. The subject value MUST either be scoped to be locally unique
 *         in the context of the issuer or be globally unique. The processing of
 *         this claim is generally application specific. The sub value is a
 *         case-sensitive string containing a StringOrURI value. Use of this
 *         claim is OPTIONAL.
 *         </p>
 *         </li>
 * 
 * 
 *         <li>"aud" (Audience) Claim
 * 
 *         <p>
 *         The aud (audience) claim identifies the recipients that the JWT is
 *         intended for. Each principal intended to process the JWT MUST
 *         identify itself with a value in the audience claim. If the principal
 *         processing the claim does not identify itself with a value in the aud
 *         claim when this claim is present, then the JWT MUST be rejected. In
 *         the general case, the aud value is an array of case-sensitive
 *         strings, each containing a StringOrURI value. In the special case
 *         when the JWT has one audience, the aud value MAY be a single
 *         case-sensitive string containing a StringOrURI value. The
 *         interpretation of audience values is generally application specific.
 *         Use of this claim is OPTIONAL.
 *         </p>
 *         </li>
 * 
 *         <li>"exp" (Expiration Time) Claim
 * 
 *         <p>
 *         The exp (expiration time) claim identifies the expiration time on or
 *         after which the JWT MUST NOT be accepted for processing. The
 *         processing of the exp claim requires that the current date/time MUST
 *         be before the expiration date/time listed in the exp claim.
 *         Implementers MAY provide for some small leeway, usually no more than
 *         a few minutes, to account for clock skew. Its value MUST be a number
 *         containing a NumericDate value. Use of this claim is OPTIONAL.
 *         </p>
 *         </li>
 * 
 *         <li>"nbf" (Not Before) Claim
 * 
 *         <p>
 *         The nbf (not before) claim identifies the time before which the JWT
 *         MUST NOT be accepted for processing. The processing of the nbf claim
 *         requires that the current date/time MUST be after or equal to the
 *         not-before date/time listed in the nbf claim. Implementers MAY
 *         provide for some small leeway, usually no more than a few minutes, to
 *         account for clock skew. Its value MUST be a number containing a
 *         NumericDate value. Use of this claim is OPTIONAL.
 *         </p>
 *         </li>
 * 
 *         <li>"iat" (Issued At) Claim
 * 
 *         <p>
 *         The iat (issued at) claim identifies the time at which the JWT was
 *         issued. This claim can be used to determine the age of the JWT. Its
 *         value MUST be a number containing a NumericDate value. Use of this
 *         claim is OPTIONAL.
 *         </p>
 *         </li>
 * 
 *         <li>"jti" (JWT ID) Claim
 * 
 *         <p>
 *         The jti (JWT ID) claim provides a unique identifier for the JWT. The
 *         identifier value MUST be assigned in a manner that ensures that there
 *         is a negligible probability that the same value will be accidentally
 *         assigned to a different data object; if the application uses multiple
 *         issuers, collisions MUST be prevented among values produced by
 *         different issuers as well. The jti claim can be used to prevent the
 *         JWT from being replayed. The jti value is a case-sensitive string.
 *         Use of this claim is OPTIONAL.
 *         </p>
 *         </li>
 * 
 *         </ul>
 *
 */
@Service
public class TokenService {

	private static final Options SET_JWT_OPTIONS = new JWTSigner.Options()
				.setExpirySeconds(30 * 60).setNotValidBeforeLeeway(5)
				.setIssuedAt(true).setJwtId(true);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

	@Value("${app.jwt.secret}")
	private String secret;
	
	@Value("${app.jwt.iss}")
	private String iss;
	
	@Value("${app.jwt.aud}")
	private String aud;
	
	private JWTSigner jwtSigner; 
	
	private JWTVerifier jwtVerifier; 
	
	/**
	 * Initializes the JWT signer and verifier.
	 */
	@PostConstruct
	public void init(){
		jwtSigner = new JWTSigner(secret);
		jwtVerifier = new JWTVerifier(secret,aud,iss);
	}

	
	/**
	 * It takes a subject and uses it to create a JWT token specific to that subject.
	 * Other claim items are used as specified in configuration file.
	 * 
	 * @param subject for which token is generated and maps to 'sub' claim and 'role' claim.
	 * @return JWT generated token
	 * @throws IllegalArgumentException if the subject passed in is empty
	 */
	public String createToken(User subject) {
		if(subject == null || subject.name.trim().length() == 0){
			throw new IllegalArgumentException("Subject cannot be null or name empty.");
		}
		
		LOGGER.info("Generating token for subject {}",subject);
		
		Map<String, Object> claims = new HashMap<String, Object>(); 
		claims.put("iss", iss);
		claims.put("sub", subject.name);
		claims.put("aud", aud);
		claims.put("role", subject.role);
		
		return jwtSigner.sign(claims,SET_JWT_OPTIONS);
	}
	
	
	/**
	 * It takes a JWT encrypted token, decrypts it and returns a Map of cliams associated with the token
	 * 
	 * @param token JWT
	 * @return Map of claims decrypted from token
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws JWTVerifyException
	 */
	public Map<String, Object> verify(String token) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			SignatureException, IOException, JWTVerifyException {
		return jwtVerifier.verify(token);
	}
}
