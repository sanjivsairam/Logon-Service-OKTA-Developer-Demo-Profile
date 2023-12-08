/**
 * 
 */
package com.demoagro.loginservice.service.impl;

import java.time.Instant;
import java.util.UUID;

import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.demoagro.loginservice.exception.LoginServiceException;
import com.demoagro.loginservice.jwt.JwtUtils;
import com.demoagro.loginservice.model.common.UserDetailsImpl;
import com.demoagro.loginservice.model.entity.User;
import com.demoagro.loginservice.model.entity.UserSession;
import com.demoagro.loginservice.model.request.AuditInfo;
import com.demoagro.loginservice.model.request.LoginRequest;
import com.demoagro.loginservice.repository.UserRepository;
import com.demoagro.loginservice.repository.UserSessionRepository;
import com.demoagro.loginservice.service.interfaces.AuditLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demoagro.loginservice.model.response.LoginResponse;
import com.demoagro.loginservice.service.interfaces.LoginService;

@Service
/**
 * @author rsairam
 *
 */
public class LoginServiceImpl implements LoginService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private JwtUtils jwtUtils;

	@Value("${loginservice.jwt.refresh.token.ExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private AuditLoggingService auditLoggingService;

	/**
	 *
	 */
	@Override
	@Transactional
	public LoginResponse loadUser(LoginRequest loginRequest) {

		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getEmail()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		final String jwt = jwtUtils.generateJwtToken(userDetails);
		// We need to create refresh token here and save to DB
		final String refreshToken = UUID.randomUUID().toString();
		User user = userRepository.getById(userDetails.getUserId());
		UserSession userSession = prepareUserSession(user, jwt, refreshToken);
		userSessionRepository.save(userSession);
		auditLoggingService.createAuditLogging(prepareLoginAuditInfo(userDetails));
		final LoginResponse loginResponse = prepareResponse(userDetails, jwt, refreshToken);

		return loginResponse;
	}

	/**
	 *
	 */
	@Override
	public User loadUser(String userName) throws UsernameNotFoundException {

		User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(
				LoginServiceErrorMessageConstants.USER_NOT_FOUND_WITH_USERNAME + userName));

		return user;
	}

	/**
	 * @param userId
	 * @param user
	 * @return
	 */
	private UserSession prepareUserSession(User user, String jwtToken, String refreshToken) {
		UserSession userSession = user.getUserSession();
		if (null != userSession) {
			userSessionRepository.delete(userSession);
		}
		userSession = new UserSession();
		user.setUserSession(userSession);
		userSession.setUser(user);
		userSession.setExpiryTime(Instant.now().plusMillis(refreshTokenDurationMs));
		userSession.setLoginTime(Instant.now());
		userSession.setRefreshToken(refreshToken);
		userSession.setSessionId(UUID.randomUUID().toString());
		userSession.setStatus("Success");
		userSession.setTokenCreationTime(Instant.now());
		userSession.setUserToken(jwtToken);
		userSession.setErrorMessage("NA");
		return userSession;
	}

	/**
	 * @param userDetails
	 * @param jwt
	 * @param refreshToken
	 */
	private LoginResponse prepareResponse(UserDetailsImpl userDetails, String jwt, final String refreshToken) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUserId(userDetails.getUserId());
		loginResponse.setName(userDetails.getName());
		loginResponse.setEmail(userDetails.getEmail());
		loginResponse.setProfile(userDetails.getProfile());
		loginResponse.setJobTitle(userDetails.getJobTitle());
		loginResponse.setNotifications(userDetails.isNotifications());
		loginResponse.setToken(jwt);
		loginResponse.setRefreshToken(refreshToken);
		return loginResponse;
	}

	/**
	 *
	 */
	@Override
	public void deleteUserSession(final Long userId) {
		final UserSession userSession = userSessionRepository.findByUser_UserId(userId).orElseThrow(
				() -> new LoginServiceException(LoginServiceErrorMessageConstants.USER_NOT_FOUND_WITH_ID + userId));
		userSessionRepository.delete(userSession);
		auditLoggingService.createAuditLogging(prepareLogoutAuditInfo(userSession));
	}

	/**
	 * @param userSession
	 * @return
	 */
	private AuditInfo prepareLogoutAuditInfo(final UserSession userSession) {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setDescription("User logout activity");
		auditInfo.setLastModifiedBy(userSession.getUser().getName());
		auditInfo.setLastModifiedOn(Instant.now());
		auditInfo.setScreen("Login Screen");
		auditInfo.setUserName(userSession.getUser().getName());

		return auditInfo;
	}

	/**
	 * @param userDetails
	 * @return
	 */
	private AuditInfo prepareLoginAuditInfo(final UserDetailsImpl userDetails) {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setDescription("User login activity");
		auditInfo.setLastModifiedBy(userDetails.getName());
		auditInfo.setLastModifiedOn(Instant.now());
		auditInfo.setScreen("Login Screen");
		auditInfo.setUserName(userDetails.getName());

		return auditInfo;
	}
}
