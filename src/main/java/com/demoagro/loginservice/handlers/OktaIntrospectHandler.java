/**
 * 
 */
package com.demoagro.loginservice.handlers;

import com.demoagro.loginservice.exception.LoginServiceException;
import com.demoagro.loginservice.service.interfaces.OktaTokenIntrospect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demoagro.loginservice.model.response.OktaIntrospectResponse;

@Component
/**
 * @author rsairam
 *
 */
public class OktaIntrospectHandler {

	@Autowired
	private OktaTokenIntrospect oktaTokenIntrospect;

	@Value("${loginservice.okta.token.clientId}")
	private String oktaClientId;

	@Value("${loginservice.okta.token.type.hint}")
	private String oktaTokenTypehint;

	/**
	 * @param token
	 */
	public void validateOktaSSOToken(final String token) {

		final OktaIntrospectResponse oktaIntrospectResponse = oktaTokenIntrospect.introspectOktaToken(oktaClientId, token,
				oktaTokenTypehint);
		if (!oktaIntrospectResponse.isActive()) {
			throw new LoginServiceException("Okta SSO token is expired. Please do Okta SSO login again!");
		}
	}
}
