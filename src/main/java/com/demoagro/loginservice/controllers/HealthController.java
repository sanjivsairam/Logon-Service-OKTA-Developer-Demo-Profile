package com.demoagro.loginservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@CrossOrigin
public class HealthController {
	
    /**
     * @return
     */
    @GetMapping
    public ResponseEntity<String> getDefault() {
        return new ResponseEntity<String>("LoginService is up and running)", HttpStatus.OK);
    }
}
