package com.demoagro.loginservice.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demoagro.loginservice.model.common.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demoagro.loginservice.model.response.ApplicationResponse;
import com.demoagro.loginservice.model.response.LoginResponse;

/**
 * @author rsairam
 *
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 *
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		ErrorMessage errorMessage = new ErrorMessage(HttpServletResponse.SC_UNAUTHORIZED, new Date(),
				authException.getMessage(), request.getServletPath());

		ApplicationResponse<LoginResponse> errorResponse = new ApplicationResponse<LoginResponse>(null, errorMessage);
		objectMapper.writeValue(response.getOutputStream(), errorResponse);

	}

}
