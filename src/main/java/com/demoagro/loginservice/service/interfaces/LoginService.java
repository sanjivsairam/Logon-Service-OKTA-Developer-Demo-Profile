/**
 * 
 */
package com.demoagro.loginservice.service.interfaces;

import com.demoagro.loginservice.model.entity.User;
import com.demoagro.loginservice.model.request.LoginRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.demoagro.loginservice.model.response.LoginResponse;

/**
 * @author rsairam
 *
 */
public interface LoginService {

	public LoginResponse loadUser(LoginRequest loginRequest);

	public User loadUser(String userName) throws UsernameNotFoundException;
	
	public void deleteUserSession(final Long userId);
	
}
