/**
 * 
 */
package com.demoagro.loginservice.constants;

/**
 * @author rsairam
 *
 */
public class LoginServiceErrorMessageConstants {

	private LoginServiceErrorMessageConstants() {

	}
	public static final String HEADER_IS_NOT_PRESENT_OR_INVALID = "header is not present or invalid";
	
	public static final String TOKEN_EXPIRED_OR_INVALID = "Token expired or invalid";

	public static final String USER_SESSION_LOGGED_OUT_PLEASE_LOGIN_AGAIN = "User session logged out. Please login again";
	
	public static final String EMAIL_SHOULD_NOT_BE_NULL_OR_EMPTY = "email should not be null or empty";
	
	public static final String REFRESH_TOKEN_SHOULD_NOT_BE_NULL_OR_EMPTY = "refreshToken should not be null or empty";
	
	public static final String USER_JOB_TITLE_SHOULD_NOT_BE_NULL_OR_EMPTY = "User jobTitle should not be null or empty";

	public static final String USER_PROFILE_SHOULD_NOT_BE_NULL_OR_EMPTY = "User profile should not be null or empty";

	public static final String USER_EMAIL_SHOULD_NOT_BE_NULL_EMPTY_OR_INVALID_FORMAT = "User email should not be null, empty or invalid format";

	public static final String USER_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY = "User name should not be null or empty";
	
	public static final String USER_ID_SHOULD_NOT_BE_NULL_OR_EMPTY = "User Id should not be null or empty";
	
	public static final String BEARER = "Bearer";
	
	public static final String USER_NOT_FOUND_WITH_USERNAME = "User Not Found with username: ";
	
	public static final String USER_NOT_FOUND_WITH_ID = "User Not Found with Id: ";

	public static final String REFRESH_TOKEN_WAS_EXPIRED_PLEASE_MAKE_A_NEW_SIGNIN_REQUEST = "Refresh token was expired. Please make a new signin request";

	public static final String REFRESH_TOKEN_IS_NOT_IN_DATABASE = "Refresh token is not found or invalid!";

	public static final String UNABLE_TO_GENERATE_TOKEN_PLEASE_TRY_AGAIN_LATER = "Unable to generate token. Please try again later!";
	
	public static final String USER_EMAIL_ALREADY_PRESENT_PLEASE_TRY_WITH_DIFFERENT = "User Email already present. Please try with different";


}
