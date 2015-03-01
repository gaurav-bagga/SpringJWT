package com.green.jwt.security;

import java.util.Map;

import com.green.jwt.model.Role;
import com.green.jwt.model.User;

/**
 * Keeps the JWT cliams in thread local variable, so that its available to all execution thread methods.
 * 
 * @author gaurav.bagga
 *
 */
public final class SecurityContext {
	
	private static final ThreadLocal<Subject> context = new ThreadLocal<SecurityContext.Subject>();
	
	/**
	 * Entity object populated from JWT claims map.
	 * 
	 * @author gaurav.bagga
	 *
	 */
	public static final class Subject {
		
		private final String token;
		private final String issuer;
		private final String audience;
		private final String subject;
		private final Map<String, Object> claims;
		private final User user;
		
		public Subject(String token,Map<String, Object> claims){
			this.token = token;
			this.claims = claims;
			this.issuer = claims.get("iss").toString();
			this.audience = claims.get("aud").toString();
			this.subject = claims.get("sub").toString();
			Role role = claims.get("role") == null ? null : Role.valueOf(claims.get("role").toString());
			this.user = new User(subject, null, role);
		}
		
		public String getToken(){
			return this.token;
		}

		public String getIssuer() {
			return issuer;
		}

		public String getAudience() {
			return audience;
		}

		public String getSubject() {
			return subject;
		}

		public Map<String, Object> getClaims() {
			return claims;
		}

		public User getUser() {
			return user;
		}

	}
	
	public static Subject getContextSubject(){
		return context.get();
	}
	
	public static void setContextSubject(String token,Map<String, Object> claims){
		if(context.get() == null){
			context.set(new Subject(token,claims));
		}
	}
	
	public static void clearContextSubject(){
		context.remove();
	}

}
