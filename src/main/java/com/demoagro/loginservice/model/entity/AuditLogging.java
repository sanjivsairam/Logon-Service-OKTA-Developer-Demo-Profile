package com.demoagro.loginservice.model.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author rsairam
 *
 */
@Entity
@Table(name = "audit_logging", schema = "logon_service_schema")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditLogging {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auditId")
	private Long auditId;

	@Column(name = "userName")
	private String userName;

	@Column(name = "description")
	private String description;

	@Column(name = "screen")
	private String screen;

	@Column(name = "lastModifiedOn")
	private Instant lastModifiedOn;

	@Column(name = "lastModifiedBy")
	private String lastModifiedBy;
	
	@Column(name = "lastModifiedDate")
	private Instant lastModifiedDate;

	/**
	 * @return the auditId
	 */
	public Long getAuditId() {
		return auditId;
	}

	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the screen
	 */
	public String getScreen() {
		return screen;
	}

	/**
	 * @param screen the screen to set
	 */
	public void setScreen(String screen) {
		this.screen = screen;
	}

	/**
	 * @return the lastModifiedOn
	 */
	public Instant getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public void setLastModifiedOn(Instant lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * @return the lastModifiedDate
	 */

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
