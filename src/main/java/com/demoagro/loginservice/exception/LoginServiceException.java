/**
 * 
 */
package com.demoagro.loginservice.exception;

/**
 * @author rsairam
 *
 */
public class LoginServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public LoginServiceException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
	
	public LoginServiceException(String errorMessage) {
		super(errorMessage);
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
	
	

}
