package com.demoagro.loginservice.model.request;

import java.time.Instant;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * @author rsairam
 *
 */
public class AuditInfo {
	private Long auditId;
	
	@NotBlank(message = "User name should not be null or empty")
	private String userName;
	
	@NotBlank(message = "Description should not be null or empty")
	private String description;
	
	@NotBlank(message = "Screen should not be null or empty")
	private String screen;
	
	private Instant lastModifiedOn = Instant.now();
	
	private String lastModifiedBy;

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public Instant getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Instant lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	
}
