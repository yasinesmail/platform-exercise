package com.fenderdigital.platform.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -7592248173696558765L;

	public UserNotFoundException(String exception) {
	    super(exception);
	}
}
