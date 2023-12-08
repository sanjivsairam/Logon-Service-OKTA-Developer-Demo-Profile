package com.demoagro.loginservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author rsairam
 *
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
 * @param message
 */
public TokenRefreshException(String message) {
    super(message);
  }
}
