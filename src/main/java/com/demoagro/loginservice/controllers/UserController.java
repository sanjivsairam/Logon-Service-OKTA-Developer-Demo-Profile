/**
 * 
 */
package com.demoagro.loginservice.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoagro.loginservice.constants.LoginServiceConstants;
import com.demoagro.loginservice.model.request.UserInfo;
import com.demoagro.loginservice.model.request.UserUpdateRequest;
import com.demoagro.loginservice.model.response.ApplicationResponse;
import com.demoagro.loginservice.service.interfaces.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(LoginServiceConstants.USER)
/**
 * @author rsairam
 *
 */
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * @param userInfo
	 * @return
	 */
	@PostMapping(value = LoginServiceConstants.CREATE)
	@PreAuthorize(LoginServiceConstants.HAS_ROLE_ADMIN)
	public ResponseEntity<ApplicationResponse<UserInfo>> createUser(@Valid @RequestBody UserInfo userInfo) {

		userInfo = userService.createUser(userInfo);
		return new ResponseEntity<ApplicationResponse<UserInfo>>(new ApplicationResponse<UserInfo>(userInfo, null),
				HttpStatus.CREATED);
	}

	/**
	 * @param userUpdateRequest
	 * @return
	 */
	@PutMapping(value = LoginServiceConstants.UPDATE)
	@PreAuthorize(LoginServiceConstants.HAS_ROLE_ADMIN)
	public ResponseEntity<ApplicationResponse<UserUpdateRequest>> updateUser(
			@Valid @RequestBody UserUpdateRequest userUpdateRequest) {

		userService.updateUser(userUpdateRequest);
		return new ResponseEntity<ApplicationResponse<UserUpdateRequest>>(
				new ApplicationResponse<UserUpdateRequest>(userUpdateRequest, null), HttpStatus.OK);
	}

	/**
	 * @param userId
	 * @return
	 */
	@GetMapping(value = LoginServiceConstants.USER_ID)
	@PreAuthorize(LoginServiceConstants.HAS_ROLE_ADMIN)
	public ResponseEntity<ApplicationResponse<UserInfo>> getUser(@PathVariable Long userId) {
		UserInfo userInfo = userService.getUser(userId);
		return new ResponseEntity<ApplicationResponse<UserInfo>>(new ApplicationResponse<UserInfo>(userInfo, null),
				HttpStatus.OK);
	}

	/**
	 * @return
	 */
	@GetMapping(value = LoginServiceConstants.ALL)
	@PreAuthorize("hasAnyRole('ADMIN','PLANNER')")
	public ResponseEntity<ApplicationResponse<List<UserInfo>>> getAllUsers() {
		List<UserInfo> usersList = userService.getAllUser();
		return new ResponseEntity<ApplicationResponse<List<UserInfo>>>(
				new ApplicationResponse<List<UserInfo>>(usersList, null), HttpStatus.OK);
	}

	/**
	 * @param userId
	 * @return
	 */
	@DeleteMapping(value = LoginServiceConstants.DELETE_USER_ID)
	@PreAuthorize(LoginServiceConstants.HAS_ROLE_ADMIN)
	public ResponseEntity<ApplicationResponse<String>> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<ApplicationResponse<String>>(new ApplicationResponse<String>(), HttpStatus.OK);
	}
}
