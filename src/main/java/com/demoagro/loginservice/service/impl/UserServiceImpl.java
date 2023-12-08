/**
 * 
 */
package com.demoagro.loginservice.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.demoagro.loginservice.exception.LoginServiceException;
import com.demoagro.loginservice.model.common.UserDetailsImpl;
import com.demoagro.loginservice.model.entity.User;
import com.demoagro.loginservice.model.request.AuditInfo;
import com.demoagro.loginservice.repository.UserRepository;
import com.demoagro.loginservice.service.interfaces.AuditLoggingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demoagro.loginservice.model.request.UserInfo;
import com.demoagro.loginservice.model.request.UserUpdateRequest;
import com.demoagro.loginservice.service.interfaces.UserService;

@Service
/**
 * @author rsairam
 *
 */
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AuditLoggingService auditLoggingService;

	/**
	 *
	 */
	@Override
	public UserInfo createUser(UserInfo userInfo) {
		isEmailAlreadyPresent(userInfo.getEmail());
		User user = new User();
		BeanUtils.copyProperties(userInfo, user);
		user.setUserName(userInfo.getEmail());
		user.setPassword(bCryptPasswordEncoder.encode(userInfo.getEmail()));
		user = userRepository.save(user);
		createEventAudit(userInfo);
		userInfo.setUserId(user.getUserId());
		return userInfo;
	}

	/**
	 *
	 */
	@Override
	public UserUpdateRequest updateUser(UserUpdateRequest userUpdateRequest) {
		User userEntity = userRepository.findById(userUpdateRequest.getUserId())
				.orElseThrow(() -> new LoginServiceException(
						LoginServiceErrorMessageConstants.USER_NOT_FOUND_WITH_USERNAME + userUpdateRequest.getName()));
		validateEmail(userUpdateRequest.getEmail(), userEntity.getEmail());
		userEntity.setName(userUpdateRequest.getName());
		userEntity.setEmail(userUpdateRequest.getEmail());
		userEntity.setProfile(userUpdateRequest.getProfile());
		userEntity.setJobTitle(userUpdateRequest.getJobTitle());
		userEntity.setUserName(userUpdateRequest.getEmail());
		userEntity.setPassword(bCryptPasswordEncoder.encode(userUpdateRequest.getEmail()));
		userEntity.setNotifications(userUpdateRequest.isNotifications());
		userEntity = userRepository.save(userEntity);
		updateEventAudit(userUpdateRequest);
		return userUpdateRequest;
	}

	/**
	 *
	 */
	@Override
	public UserInfo getUser(Long userId) {
		User userEntity = userRepository.findById(userId).orElseThrow(
				() -> new LoginServiceException(LoginServiceErrorMessageConstants.USER_NOT_FOUND_WITH_ID + userId));
		UserInfo userInfo = new UserInfo();
		BeanUtils.copyProperties(userEntity, userInfo);
		getUserEventAudit(userInfo);
		return userInfo;
	}

	/**
	 *
	 */
	@Override
	public List<UserInfo> getAllUser() {
		List<User> user = userRepository.findAll();
		List<UserInfo> userInfoList = user.stream().map(userEntity -> {
			UserInfo userInfo = new UserInfo();
			BeanUtils.copyProperties(userEntity, userInfo);
			return userInfo;
		}).collect(Collectors.toList());

		getAllUserEventAudit();

		return userInfoList;
	}

	/**
	 *
	 */
	@Override
	public void deleteUser(Long userId) {
		final User userEntity = userRepository.findById(userId).orElseThrow(
				() -> new LoginServiceException(LoginServiceErrorMessageConstants.USER_NOT_FOUND_WITH_ID + userId));
		userRepository.delete(userEntity);
		deleteEventUpdate(userEntity);
	}

	private void validateEmail(final String updateEmailId, final String existingEmailId) {

		if (!StringUtils.equalsIgnoreCase(updateEmailId, existingEmailId)) {
			isEmailAlreadyPresent(updateEmailId);
		}
	}

	/**
	 * @param userInfo
	 */
	private void isEmailAlreadyPresent(final String emailId) {
		if (isEmailAvailable(emailId)) {
			throw new LoginServiceException(
					LoginServiceErrorMessageConstants.USER_EMAIL_ALREADY_PRESENT_PLEASE_TRY_WITH_DIFFERENT, null);
		}
	}

	/**
	 * @param userInfo
	 */
	private void createEventAudit(UserInfo userInfo) {
		final String loggedInUserName = fetchLoggendInUserName();
		auditLoggingService.createAuditLogging(
				prepareAuditInfo(loggedInUserName + " has Registered user with Name " + userInfo.getName(),
						loggedInUserName, "User Registration Screen"));
	}

	/**
	 * @param userUpdateRequest
	 */
	private void updateEventAudit(UserUpdateRequest userUpdateRequest) {
		final String loggedInUserName = fetchLoggendInUserName();
		auditLoggingService.createAuditLogging(
				prepareAuditInfo(loggedInUserName + " has updated user with Name " + userUpdateRequest.getName(),
						loggedInUserName, "User Update Screen"));
	}

	/**
	 * @param userInfo
	 */
	private void getUserEventAudit(UserInfo userInfo) {
		final String loggedInUserName = fetchLoggendInUserName();
		auditLoggingService.createAuditLogging(
				prepareAuditInfo(loggedInUserName + " has retrieved data for User " + userInfo.getName(),
						loggedInUserName, "User Update/Details Screen"));
	}

	/**
	 * 
	 */
	private void getAllUserEventAudit() {
		final String loggedInUserName = fetchLoggendInUserName();
		auditLoggingService.createAuditLogging(prepareAuditInfo(loggedInUserName + " has retrieved all Users data",
				loggedInUserName, "User list Screen"));
	}

	/**
	 * @param userEntity
	 */
	private void deleteEventUpdate(User userEntity) {
		final String loggedInUserName = fetchLoggendInUserName();
		auditLoggingService
				.createAuditLogging(prepareAuditInfo(loggedInUserName + " has deleted User " + userEntity.getName(),
						loggedInUserName, "User delete Screen"));
	}

	@Override
	public boolean isEmailAvailable(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	/**
	 * @param description
	 * @param userName
	 * @param screen
	 * @return
	 */
	private AuditInfo prepareAuditInfo(final String description, final String userName, final String screen) {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setDescription(description);
		auditInfo.setLastModifiedBy(userName);
		auditInfo.setLastModifiedOn(Instant.now());
		auditInfo.setScreen(screen);
		auditInfo.setUserName(userName);
		return auditInfo;
	}

	/**
	 * @return
	 */
	private String fetchLoggendInUserName() {
		final UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return userDetailsImpl.getName();

	}

}
