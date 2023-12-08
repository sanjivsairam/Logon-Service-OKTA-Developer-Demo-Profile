/**
 * 
 */
package com.demoagro.loginservice.model.response;

import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.demoagro.loginservice.model.request.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * @author rsairam
 *
 */
public class LoginResponse extends UserInfo {

	private String token;
	private String type = LoginServiceErrorMessageConstants.BEARER;
	private String refreshToken;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
