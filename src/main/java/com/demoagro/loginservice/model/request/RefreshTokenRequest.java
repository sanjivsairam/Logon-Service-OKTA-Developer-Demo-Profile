/**
 * 
 */
package com.demoagro.loginservice.model.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * @author rsairam
 *
 */
public class RefreshTokenRequest {
	
	
	@NotBlank(message = LoginServiceErrorMessageConstants.REFRESH_TOKEN_SHOULD_NOT_BE_NULL_OR_EMPTY)
	private String refreshToken;

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	
}
