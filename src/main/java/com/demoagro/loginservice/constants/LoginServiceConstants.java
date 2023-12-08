/**
 * 
 */
package com.demoagro.loginservice.constants;

/**
 * @author rsairam
 *
 */
public class LoginServiceConstants {

	private LoginServiceConstants() {

	}

	public static final String BEARER = "Bearer ";

	public static final String OKTA_ACCESS_TOKEN = "oktaAccessToken";

	public static final String AUTH = "/auth";

	public static final String EMAIL_AVAILABLE = "/email/available";

	public static final String REFRESH_TOKEN = "/refreshToken";

	public static final String TOKEN = "/token";

	public static final String AUTH_EMAIL_AVAILABLE = AUTH + EMAIL_AVAILABLE;

	public static final String AUTH_REFRESH_TOKEN = AUTH + REFRESH_TOKEN;

	public static final String AUTH_TOKEN = AUTH + TOKEN;

	public static final String EMAIL = "email";

	public static final String LOGOUT = "/logout";

	public static final String AUTH_LOGOUT = LOGOUT + "/{userId}";

	public static final String DELETE_USER_ID = "delete/{userId}";

	public static final String ALL = "all";

	public static final String USER_ID = "{userId}";

	public static final String UPDATE = "update";

	public static final String CREATE = "create";

	public static final String USER = "user";

	public static final String HAS_ROLE_ADMIN = "hasRole('ADMIN')";

	public static final String ROLE = "ROLE_";

	public static final String CLIENT_ID = "client_id";

	public static final String OKTA_TOKEN = "token";

	public static final String OKTA_TOKEN_TYPE_HINT = "token_type_hint";
	
	public static final String AUDIT_API_PATH = "/audit/create";
	
	public static final String AUDIT_API_PATH_ANT_MATCHER = "/audit/**";
	
	public static final String LOGGED_IN_USER = "LOGGED_IN_USER";

	public static final String ROLE_KEY = "ROLE";

}
