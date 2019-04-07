package com.fenderdigital.platform.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserRequestDto implements Serializable {
	
	private static final long serialVersionUID = -4005360045475717828L;
	
	private String name;
	private String email;
	private String password;

}
