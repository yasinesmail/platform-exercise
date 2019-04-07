package com.fenderdigital.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -8321538436614303301L;

	public UserAlreadyExistsException(String exception) {
	    super(exception);
	}
}
