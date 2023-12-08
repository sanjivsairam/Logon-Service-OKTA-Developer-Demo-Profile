/**
 * 
 */
package com.demoagro.loginservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
/**
 * @author rsairam
 *
 */
public class AppConfig {

	/**
	 * @return ObjectMapper
	 */
	@Bean
	public ObjectMapper objectMapper() {
		return JsonMapper.builder().addModule(new JavaTimeModule()).build();
	}

	@Bean
	public com.fasterxml.jackson.databind.Module dateTimeModule() {
		return new JavaTimeModule();
	}
}
