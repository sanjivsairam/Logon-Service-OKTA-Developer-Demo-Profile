package com.demoagro.loginservice.jwt;

import java.util.Date;

import com.demoagro.loginservice.model.common.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demoagro.loginservice.constants.LoginServiceConstants;
import com.demoagro.loginservice.exception.LoginServiceException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author rsairam
 *
 */
@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${loginservice.jwt.token.secret}")
	private String jwtSecret;

	@Value("${loginservice.jwt.token.expirationMs}")
	private int jwtExpirationMs;

	/**
	 * @param userPrincipal
	 * @return
	 */
	public String generateJwtToken(UserDetailsImpl userPrincipal) {
		return generateTokenFromUsername(userPrincipal.getUsername(), userPrincipal.getProfile(), userPrincipal.getName());
	}

	/**
	 * @param username
	 * @return
	 */
	public String generateTokenFromUsername(String userName, String role, String name) {
		Claims claims = Jwts.claims().setSubject(userName);
		claims.put(LoginServiceConstants.ROLE_KEY, role);
		claims.put(LoginServiceConstants.LOGGED_IN_USER, name);
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	/**
	 * @param token
	 * @return
	 */
	public String getUserNameFromJwtToken(String token) {
		return parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * @param authToken
	 * @return
	 */
	public boolean validateJwtToken(String authToken) {
		try {
			parseClaimsJws(authToken);
			return true;
		} catch (LoginServiceException exception) {
			logger.error("JWT token valdation failed: {}", exception.getMessage());
			throw exception;
		}
	}

	/**
	 * @param token
	 * @return
	 */
	private Jws<Claims> parseClaimsJws(String token) {
		try {
			return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				| IllegalArgumentException exception) {
			exception.printStackTrace();
			logger.error("JWT token valdation failed: {}", exception.getMessage());
			throw new LoginServiceException("Token expired or invalid", exception);
		}

	}

}
