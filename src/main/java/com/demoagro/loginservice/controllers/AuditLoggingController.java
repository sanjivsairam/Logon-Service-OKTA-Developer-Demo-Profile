package com.demoagro.loginservice.controllers;

import javax.validation.Valid;

import com.demoagro.loginservice.model.request.AuditInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.demoagro.loginservice.model.response.ApplicationResponse;
import com.demoagro.loginservice.service.interfaces.AuditLoggingService;

/**
 * @author ssingh11323
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/audit")
public class AuditLoggingController {

	@Autowired
	private AuditLoggingService auditService;
	
	/**
	 * @param auditInfo
	 * @return
	 */
	@PostMapping(value = "/create")
	public ResponseEntity<ApplicationResponse<String>> createUser(@Valid @RequestBody final AuditInfo auditInfo) {
		auditService.createAuditLogging(auditInfo);
		return new ResponseEntity<ApplicationResponse<String>>(new ApplicationResponse<String>(), HttpStatus.CREATED);
	}
}
