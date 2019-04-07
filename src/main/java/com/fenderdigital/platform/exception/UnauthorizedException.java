package com.fenderdigital.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = -6890655718291399237L;

	public UnauthorizedException(String exception) {
	    super(exception);
	}

}
