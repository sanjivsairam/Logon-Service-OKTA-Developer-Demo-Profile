package com.demoagro.loginservice.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demoagro.loginservice.model.common.ErrorMessage;
import com.demoagro.loginservice.model.common.UserDetailsImpl;
import com.demoagro.loginservice.model.entity.User;
import com.demoagro.loginservice.model.entity.UserSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demoagro.loginservice.constants.LoginServiceConstants;
import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.demoagro.loginservice.exception.LoginServiceException;
import com.demoagro.loginservice.model.response.ApplicationResponse;
import com.demoagro.loginservice.model.response.LoginResponse;
import com.demoagro.loginservice.service.interfaces.LoginService;

@Order(2)
/**
 * @author rsairam
 *
 */
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private LoginService loginService;

	/**
	 *
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			final String jwt = parseJwt(request, HttpHeaders.AUTHORIZATION);
			validateJwtTokenExpiration(jwt);

			String username = jwtUtils.getUserNameFromJwtToken(jwt);
			User user = loginService.loadUser(username);
			UserSession userSession = user.getUserSession();
			validateJwtTokenExistanceInDb(jwt, userSession);
			UserDetails userDetails = UserDetailsImpl.build(user);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(),
					e.getMessage(), request.getRequestURI());
			ApplicationResponse<LoginResponse> errorResponse = new ApplicationResponse<LoginResponse>(null,
					errorMessage);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
			return;
		}
		filterChain.doFilter(request, response);

	}

	/**
	 * @param jwt
	 * @param userSession
	 */
	private void validateJwtTokenExistanceInDb(final String jwt, UserSession userSession) {
		if (null == userSession || !StringUtils.equals(jwt, userSession.getUserToken())) {
			throw new LoginServiceException(
					LoginServiceErrorMessageConstants.USER_SESSION_LOGGED_OUT_PLEASE_LOGIN_AGAIN);
		}
	}

	/**
	 * @param jwt
	 */
	private void validateJwtTokenExpiration(String jwt) {
		if (!jwtUtils.validateJwtToken(jwt)) {
			throw new LoginServiceException(LoginServiceErrorMessageConstants.TOKEN_EXPIRED_OR_INVALID);
		}
	}

	/**
	 *
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return StringUtils.containsAny(path, LoginServiceConstants.AUTH_TOKEN, LoginServiceConstants.AUTH_REFRESH_TOKEN,
				LoginServiceConstants.AUTH_EMAIL_AVAILABLE, (LoginServiceConstants.AUTH + LoginServiceConstants.LOGOUT),
				LoginServiceConstants.AUDIT_API_PATH);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String parseJwt(HttpServletRequest request, final String header) {
		String headerAuth = request.getHeader(header);

		if (StringUtils.isNoneBlank(headerAuth) && headerAuth.startsWith(LoginServiceConstants.BEARER)) {
			return headerAuth.substring(7, headerAuth.length());
		} else {
			throw new LoginServiceException(
					String.join(" ", header, LoginServiceErrorMessageConstants.HEADER_IS_NOT_PRESENT_OR_INVALID));
		}
	}
}
