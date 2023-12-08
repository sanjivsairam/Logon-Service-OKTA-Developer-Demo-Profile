/**
 * 
 */
package com.demoagro.loginservice.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.demoagro.loginservice.constants.UserProfileEnum;
import com.demoagro.loginservice.validators.ProfileEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * @author rsairam
 *
 */
public class UserInfo {
	
	private Long userId;
	
	@NotBlank(message = LoginServiceErrorMessageConstants.USER_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY)
	private String name;
	
	@Email(message = LoginServiceErrorMessageConstants.USER_EMAIL_SHOULD_NOT_BE_NULL_EMPTY_OR_INVALID_FORMAT)
	private String email;
	
	@NotBlank(message = LoginServiceErrorMessageConstants.USER_PROFILE_SHOULD_NOT_BE_NULL_OR_EMPTY)
	@ProfileEnum(enumClass = UserProfileEnum.class, message = "User Profile must be any of [ADMIN, PLANNER, VIEWONLY]")
	private String profile;
	
	@NotBlank(message = LoginServiceErrorMessageConstants.USER_JOB_TITLE_SHOULD_NOT_BE_NULL_OR_EMPTY)
	private String jobTitle;
	
	private boolean notifications;
	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the profile
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the notifications
	 */
	public boolean isNotifications() {
		return notifications;
	}

	/**
	 * @param notifications the notifications to set
	 */
	public void setNotifications(boolean notifications) {
		this.notifications = notifications;
	}

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
