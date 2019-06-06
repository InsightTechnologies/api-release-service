package com.miracle.release.exception;

import org.springframework.http.HttpStatus;

import com.miracle.exception.APIFrameworkException;

public class ReleaseException extends APIFrameworkException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -816387637206481014L;

	public ReleaseException() {
	}

	/**
	 * @param message
	 */
	public ReleaseException(String message) {
		super(message);
	}
	
	public ReleaseException(String message,String errorCode) {
		super(message);
		setErrorCode(errorCode);
	}
	public ReleaseException(String message,String errorCode,HttpStatus statusCode) {
		super(message);
		setErrorCode(errorCode);
		setStatusCode(statusCode);
	}

	/**
	 * @param cause
	 */
	public ReleaseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReleaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 * @param errorCode
	 */
	public ReleaseException(String message, Throwable cause,String errorCode) {
		super(message, cause);
		setErrorCode(errorCode);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause
	 * @param errorCode
	 * @param statusCode
	 */
	public ReleaseException(String message, Throwable cause,String errorCode,HttpStatus statusCode) {
		super(message, cause);
		setErrorCode(errorCode);
		setStatusCode(statusCode);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ReleaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
