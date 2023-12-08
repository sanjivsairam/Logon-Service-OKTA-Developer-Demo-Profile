package com.demoagro.loginservice.service.interfaces;

import com.demoagro.loginservice.model.request.AuditInfo;

/**
 * @author rsairam
 *
 */
public interface AuditLoggingService {

	public void createAuditLogging(AuditInfo logging);
}
