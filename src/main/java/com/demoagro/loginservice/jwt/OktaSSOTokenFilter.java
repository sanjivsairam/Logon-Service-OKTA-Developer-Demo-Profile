/**
 * 
 */
package com.demoagro.loginservice.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demoagro.loginservice.model.common.ErrorMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demoagro.loginservice.constants.LoginServiceConstants;
import com.demoagro.loginservice.exception.LoginServiceException;
import com.demoagro.loginservice.handlers.OktaIntrospectHandler;
import com.demoagro.loginservice.model.response.ApplicationResponse;
import com.demoagro.loginservice.model.response.LoginResponse;

@Component
@Order(1)
/**
 * @author rsairam
 *
 */
public class OktaSSOTokenFilter extends OncePerRequestFilter {

	@Autowired
	private OktaIntrospectHandler oktaIntrospectHandler;

	@Value("${loginservice.okta.token.validation}")
	private boolean oktaTokenValidation;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			validateOktaAccessToken(request);

		} catch (LoginServiceException loginServiceException) {
			final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), new Date(),
					loginServiceException.getMessage(), request.getRequestURI());
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
	 *
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return StringUtils.containsAny(path, LoginServiceConstants.AUTH_EMAIL_AVAILABLE,
				(LoginServiceConstants.AUTH + LoginServiceConstants.LOGOUT), LoginServiceConstants.AUDIT_API_PATH);
	}

	/**
	 * @param request
	 */
	private void validateOktaAccessToken(HttpServletRequest request) {
		if (oktaTokenValidation) {
			final String oktaSSOToken = AuthTokenFilter.parseJwt(request, LoginServiceConstants.OKTA_ACCESS_TOKEN);
			oktaIntrospectHandler.validateOktaSSOToken(oktaSSOToken);
		}
	}

}
