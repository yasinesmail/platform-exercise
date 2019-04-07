package com.fenderdigital.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = 7448075216474887267L;

	public ValidationException(String exception) {
	    super(exception);
	}
}
