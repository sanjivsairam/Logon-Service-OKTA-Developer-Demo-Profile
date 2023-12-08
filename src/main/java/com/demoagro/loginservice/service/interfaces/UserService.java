/**
 * 
 */
package com.demoagro.loginservice.service.interfaces;

import java.util.List;

import com.demoagro.loginservice.model.request.UserInfo;
import com.demoagro.loginservice.model.request.UserUpdateRequest;

/**
 * @author rsairam
 *
 */
public interface UserService {

	public UserInfo createUser(UserInfo userInfo);
	
	public UserUpdateRequest updateUser(UserUpdateRequest userUpdateRequest);
	
	public UserInfo getUser(Long userId);
	
	public List<UserInfo> getAllUser();
	
	public void deleteUser(Long userId);
	
	public boolean isEmailAvailable(String email);
	
}
