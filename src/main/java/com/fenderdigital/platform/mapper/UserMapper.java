package com.fenderdigital.platform.mapper;

import java.util.LinkedList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fenderdigital.platform.dto.TokenDto;
import com.fenderdigital.platform.dto.UserDto;
import com.fenderdigital.platform.dto.UserRequestDto;
import com.fenderdigital.platform.model.User;

@Component
public class UserMapper {
	
	private ModelMapper modelMapper = new ModelMapper();
	
	public UserDto convertUserToDto(User user) {
	    UserDto userDto = modelMapper.map(user, UserDto.class);
	    return userDto;
	}
	
	public List<UserDto> convertToDtoList(List<User> users) {
		List<UserDto> userDtos = new LinkedList<UserDto>();
		users.forEach(user->{
			userDtos.add(convertUserToDto(user));
		});
		return userDtos;
	}
	
	public User convertToDao(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
	    return user;
	}
	
	public User convertUserRequestDtoToUserDao(UserRequestDto userRequestDto ) {
		User user = modelMapper.map(userRequestDto, User.class);
	    return user;
	}
	
	public TokenDto getTokenDto(User user) {
		TokenDto tokenDto = modelMapper.map(user, TokenDto.class);
		return tokenDto;
	}

}
