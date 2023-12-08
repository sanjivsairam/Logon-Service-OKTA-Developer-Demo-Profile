package com.demoagro.loginservice.service.impl;

import java.time.Instant;

import com.demoagro.loginservice.model.entity.AuditLogging;
import com.demoagro.loginservice.model.request.AuditInfo;
import com.demoagro.loginservice.repository.AuditLoggingRepository;
import com.demoagro.loginservice.service.interfaces.AuditLoggingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rsairam
 *
 */
@Service
public class AuditLoggingServiceImpl implements AuditLoggingService {

	@Autowired
	private AuditLoggingRepository auditLoggingRepository;

	/**
	 *
	 */
	@Override
	public void createAuditLogging(AuditInfo auditInfo) {
		final AuditLogging auditLogging = new AuditLogging();
		BeanUtils.copyProperties(auditInfo, auditLogging);
		auditLogging.setLastModifiedDate(Instant.now());
		auditLoggingRepository.save(auditLogging);
	}
}
