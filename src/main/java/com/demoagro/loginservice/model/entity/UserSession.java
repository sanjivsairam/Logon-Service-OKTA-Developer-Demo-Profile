/**
 * 
 */
package com.demoagro.loginservice.model.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user_session", schema = "logon_service_schema")
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * @author rsairam
 *
 */
public class UserSession {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "loginTime")
	private Instant loginTime;
	
	@Column(name = "userToken")
	private String userToken;
	
	@Column(name = "tokenCreationTime")
	private Instant tokenCreationTime;
	
	@Column(name = "sessionId")
	private String sessionId;
	
	@Column(name = "expiryTime")
	private Instant expiryTime;
	
	@Column(name = "refreshToken")
	private String refreshToken;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "errorMessage")
	private String errorMessage;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
	private User user;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the userToken
	 */
	public String getUserToken() {
		return userToken;
	}

	/**
	 * @param userToken the userToken to set
	 */
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}


	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the loginTime
	 */
	public Instant getLoginTime() {
		return loginTime;
	}

	/**
	 * @param loginTime the loginTime to set
	 */
	public void setLoginTime(Instant loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * @return the tokenCreationTime
	 */
	public Instant getTokenCreationTime() {
		return tokenCreationTime;
	}

	/**
	 * @param tokenCreationTime the tokenCreationTime to set
	 */
	public void setTokenCreationTime(Instant tokenCreationTime) {
		this.tokenCreationTime = tokenCreationTime;
	}

	/**
	 * @return the expiryTime
	 */
	public Instant getExpiryTime() {
		return expiryTime;
	}

	/**
	 * @param expiryTime the expiryTime to set
	 */
	public void setExpiryTime(Instant expiryTime) {
		this.expiryTime = expiryTime;
	}
	
	
}
