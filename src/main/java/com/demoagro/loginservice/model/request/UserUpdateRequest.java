/**
 * 
 */
package com.demoagro.loginservice.model.request;

import javax.validation.constraints.NotNull;

import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * @author rsairam
 *
 */
public class UserUpdateRequest extends UserInfo{

	@NotNull(message = LoginServiceErrorMessageConstants.USER_ID_SHOULD_NOT_BE_NULL_OR_EMPTY)
	private Long userId;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
