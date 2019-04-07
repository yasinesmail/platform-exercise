package com.fenderdigital.platform.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserDto implements Serializable {
	
	private static final long serialVersionUID = -7605869995112068577L;
	
	private Integer id;
	private String name;
	private String email;
}
