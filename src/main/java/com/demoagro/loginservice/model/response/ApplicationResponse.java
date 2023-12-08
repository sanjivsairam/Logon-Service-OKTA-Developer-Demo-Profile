/**
 * 
 */
package com.demoagro.loginservice.model.response;

import com.demoagro.loginservice.model.common.ErrorMessage;

/**
 * @author rsairam
 *
 */
public class ApplicationResponse<T> {

	private T data;
	
	private ErrorMessage error;
	
	public ApplicationResponse() {
	}
	
	/**
	 * @param data
	 * @param error
	 */
	public ApplicationResponse(T data, ErrorMessage error) {
		super();
		this.data = data;
		this.error = error;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @return the error
	 */
	public ErrorMessage getError() {
		return error;
	}

}
