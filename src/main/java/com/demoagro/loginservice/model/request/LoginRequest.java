package com.demoagro.loginservice.model.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;

/**
 * @author rsairam
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
	
	
	@NotBlank(message = LoginServiceErrorMessageConstants.EMAIL_SHOULD_NOT_BE_NULL_OR_EMPTY)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
