package com.demoagro.loginservice.service.impl;

import java.time.Instant;
import java.util.Optional;

import com.demoagro.loginservice.constants.LoginServiceErrorMessageConstants;
import com.demoagro.loginservice.exception.TokenRefreshException;
import com.demoagro.loginservice.jwt.JwtUtils;
import com.demoagro.loginservice.model.entity.User;
import com.demoagro.loginservice.model.entity.UserSession;
import com.demoagro.loginservice.model.request.RefreshTokenRequest;
import com.demoagro.loginservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demoagro.loginservice.model.response.RefreshTokenResponse;

/**
 * @author rsairam
 *
 */
@Service
public class RefreshTokenService {

	@Value("${loginservice.jwt.refresh.token.ExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * @param refreshTokenRequest
	 * @return
	 */
	public RefreshTokenResponse getRefreshToken(RefreshTokenRequest refreshTokenRequest) {

		return this.findByToken(refreshTokenRequest.getRefreshToken()).map(this::verifyExpiration)
				.map(UserSession::getUser).map(user -> {
					String token = jwtUtils.generateTokenFromUsername(user.getUserName(), user.getProfile(), user.getName());
					RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
					refreshTokenResponse.setRefreshToken(refreshTokenRequest.getRefreshToken());
					refreshTokenResponse.setToken(token);
					updateUserSession(user, token);
					return refreshTokenResponse;
				})
				.orElseThrow(() -> new TokenRefreshException(
						LoginServiceErrorMessageConstants.UNABLE_TO_GENERATE_TOKEN_PLEASE_TRY_AGAIN_LATER + ": "
								+ refreshTokenRequest.getRefreshToken()));
	}

	/**
	 * @param user
	 * @param token
	 */
	private void updateUserSession(User user, String token) {
		UserSession userSession = user.getUserSession();
		userSession.setTokenCreationTime(Instant.now());
		userSession.setUserToken(token);
		userRepository.save(user);
	}

	/**
	 * @param token
	 * @return
	 */
	private Optional<UserSession> findByToken(String token) {
		return userRepository.findByUserSession_RefreshToken(token).map(user -> Optional.of(user.getUserSession()))
				.orElseThrow(() -> new TokenRefreshException(
						LoginServiceErrorMessageConstants.REFRESH_TOKEN_IS_NOT_IN_DATABASE + ": " + token));
	}

	/**
	 * @param userSession
	 * @return
	 */
	private UserSession verifyExpiration(UserSession userSession) {
		if (userSession.getExpiryTime().compareTo(Instant.now()) < 0) {
			// refreshTokenRepository.delete(token);
			throw new TokenRefreshException(
					LoginServiceErrorMessageConstants.REFRESH_TOKEN_WAS_EXPIRED_PLEASE_MAKE_A_NEW_SIGNIN_REQUEST + ": "
							+ userSession.getRefreshToken());
		}

		return userSession;
	}

}
