/**
 * 
 */
package com.demoagro.loginservice.service.interfaces;

import com.demoagro.loginservice.constants.LoginServiceConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demoagro.loginservice.model.response.OktaIntrospectResponse;

@FeignClient(name = "oktaTokenIntrospect", url = "${loginservice.okta.domain}", path = "${loginservice.okta.domain.basepath}")
/**
 * @author rsairam
 *
 */
public interface OktaTokenIntrospect {

	@PostMapping(value = "${loginservice.okta.domain.endpoint}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	OktaIntrospectResponse introspectOktaToken(@RequestParam(name = LoginServiceConstants.CLIENT_ID) String client_id,
			@RequestParam(name = LoginServiceConstants.OKTA_TOKEN) String token,
			@RequestParam(name = LoginServiceConstants.OKTA_TOKEN_TYPE_HINT) String token_type_hint);
}
