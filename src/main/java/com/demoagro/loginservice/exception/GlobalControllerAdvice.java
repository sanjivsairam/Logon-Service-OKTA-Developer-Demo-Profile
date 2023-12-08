package com.demoagro.loginservice.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.demoagro.loginservice.model.common.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.demoagro.loginservice.model.response.ApplicationResponse;
import com.demoagro.loginservice.model.response.LoginResponse;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = TokenRefreshException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ApplicationResponse<ErrorMessage>> handleTokenRefreshException(TokenRefreshException ex,
                                                                                         WebRequest request) {
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(),
				request.getDescription(false));

		final ApplicationResponse<ErrorMessage> errorResponse = new ApplicationResponse<>(null, errorMessage);

		return new ResponseEntity<ApplicationResponse<ErrorMessage>>(errorResponse, HttpStatus.FORBIDDEN);
	}

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<ApplicationResponse<ErrorMessage>> handleAccessDeniedException(AccessDeniedException ex,
			WebRequest request) {
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(), ex.getMessage(),
				request.getDescription(false));

		final ApplicationResponse<ErrorMessage> errorResponse = new ApplicationResponse<>(null, errorMessage);

		return new ResponseEntity<ApplicationResponse<ErrorMessage>>(errorResponse, HttpStatus.FORBIDDEN);
	}

	/**
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = LoginServiceException.class)
	public ResponseEntity<ApplicationResponse<ErrorMessage>> handleLoginServiceException(LoginServiceException ex) {
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
				ex.getMessage(), ex.getMessage());

		final ApplicationResponse<ErrorMessage> errorResponse = new ApplicationResponse<>(null, errorMessage);

		return new ResponseEntity<ApplicationResponse<ErrorMessage>>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(),
				errors.toString(), ex.getMessage());

		final ApplicationResponse<?> errorResponse = new ApplicationResponse<>(null, errorMessage);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";

		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), error,
				ex.getMessage());
		final ApplicationResponse<?> errorResponse = new ApplicationResponse<>(null, errorMessage);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		}

		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(),
				errors.toString(), ex.getMessage());

		final ApplicationResponse<?> errorResponse = new ApplicationResponse<>(null, errorMessage);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param ex
	 * @param request
	 * @return
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		String error = "Invalid data type is submitted";

		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(), error,
				ex.getMessage());
		final ApplicationResponse<?> errorResponse = new ApplicationResponse<>(null, errorMessage);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), error,
				ex.getMessage());
		final ApplicationResponse<?> errorResponse = new ApplicationResponse<>(null, errorMessage);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		Set<HttpMethod> httpMethod = ex.getSupportedHttpMethods();
		if (!CollectionUtils.isEmpty(httpMethod)) {
			httpMethod.forEach(t -> builder.append(t + " "));
		}
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.METHOD_NOT_ALLOWED.value(), new Date(),
				builder.toString(), ex.getMessage());
		final ApplicationResponse<?> errorResponse = new ApplicationResponse<>(null, errorMessage);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 *
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), new Date(),
				builder.toString(), ex.getMessage());
		final ApplicationResponse<?> errorResponse = new ApplicationResponse<>(null, errorMessage);
		return new ResponseEntity<Object>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	/**
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ApplicationResponse<LoginResponse>> handleException(Exception ex) {
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
				ex.getMessage(), ex.getMessage());

		final ApplicationResponse<LoginResponse> errorResponse = new ApplicationResponse<LoginResponse>(null,
				errorMessage);

		return new ResponseEntity<ApplicationResponse<LoginResponse>>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
