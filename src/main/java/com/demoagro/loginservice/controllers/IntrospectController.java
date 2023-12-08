package com.demoagro.loginservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demoagro.loginservice.model.response.ApplicationResponse;

/**
 * @author rsairam
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/introspect")
public class IntrospectController {

	/**
	 * @param auditInfo
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ApplicationResponse<Boolean>> createUser() {
		return new ResponseEntity<>(new ApplicationResponse<>(true, null), HttpStatus.OK);
	}
}
