package com.fenderdigital.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fenderdigital.platform.dto.TokenDto;
import com.fenderdigital.platform.dto.UserDto;
import com.fenderdigital.platform.dto.UserRequestDto;
import com.fenderdigital.platform.exception.UnauthorizedException;
import com.fenderdigital.platform.exception.UserAlreadyExistsException;
import com.fenderdigital.platform.exception.UserNotFoundException;
import com.fenderdigital.platform.exception.ValidationException;
import com.fenderdigital.platform.mapper.UserMapper;
import com.fenderdigital.platform.model.User;
import com.fenderdigital.platform.service.UserService;


@RestController
@RequestMapping(path="/user", produces=MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	
	private final String INVALID_TOKEN = "Invalid token for id : ";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	
	/* function : userRegistration
	 * action   : POST
	 * path     : /user
	 * desc     : Create a new user in database.
	 */
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto userRegistration(
    		@RequestBody UserRequestDto userRequestDto) throws Exception {
		
		// validate the email and password before creating a new user
		if (userRequestDto.getEmail().isEmpty()) {
			throw new ValidationException("Email not specified.");
		}
		
		if (userRequestDto.getPassword().isEmpty()) {
			throw new ValidationException("Password not specified.");
		}
	
        try {
        	User userDao = userMapper.convertUserRequestDtoToUserDao(userRequestDto);
			User userDaoSaved = userService.addUser(userDao);
			return userMapper.convertUserToDto(userDaoSaved);
        } catch (UserAlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
    }
	
	
	/* function : userToken
	 * action   : POST
	 * path     : /user/token
	 * desc     : Generate a new token given the username and password.
	 */
	@PostMapping
	@RequestMapping(path="/token", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TokenDto userToken(
    		@RequestBody UserRequestDto userRequestDto) throws Exception {
		
		// validate the email and password and create a new token,
		// however, if already created then return old token
		if (userRequestDto.getEmail().isEmpty()) {
			throw new ValidationException("Email not specified.");
		}
		
		if (userRequestDto.getPassword().isEmpty()) {
			throw new ValidationException("Password not specified.");
		}
	
        try {
        	User userDao = userMapper.convertUserRequestDtoToUserDao(userRequestDto);
			User userDaoSaved = userService.getToken(userDao);
			return userMapper.getTokenDto(userDaoSaved);
        } catch (UserAlreadyExistsException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
    }
	
	
	/* function : userLogout
	 * action   : PUT
	 * path     : /user/{id}/logout
	 * desc     : Makes the token field to null in db 
	 *            if the header matches the token in db.
	 */
	@PutMapping("{id}/logout")
    public UserDto userLogout(
    		@PathVariable Integer id,
    		@RequestHeader(value="token", defaultValue="") String token) throws Exception {
		
		// perform token validation before calling services
		if (userService.validateToken(id, token) == Boolean.FALSE) {
			throw new UnauthorizedException(INVALID_TOKEN + id);
		}

		try {
        	User loggedOutUser = userService.updateToken(id);
			return userMapper.convertUserToDto(loggedOutUser);
		} catch (Exception e) {
			throw e;
		}
    }
	
	
	/* function : updateUser
	 * action   : PUT
	 * path     : /user/{id}
	 * desc     : Updates following fields, name, email and password, 
	 *            if the header matches the token in db.
	 */
	@PutMapping("{id}")
    public UserDto updateUser(
    		@PathVariable Integer id,
    		@RequestHeader(value="token", defaultValue="") String token,
    		@RequestBody UserRequestDto userRequestDto) throws Exception {
		
		// perform token validation before calling services
		if (userService.validateToken(id, token) == Boolean.FALSE) {
			throw new UnauthorizedException(INVALID_TOKEN + id);
		}
		
		try {
        	User userDao = userMapper.convertUserRequestDtoToUserDao(userRequestDto);
        	userDao.setId(id);
        	User saveUser = userService.updateUser(userDao);
			return userMapper.convertUserToDto(saveUser);
		} catch (UserNotFoundException e) {
			throw e;
		}
		catch (Exception e) {
			throw e;
		}
    }
	
	
	/* function : deleteUser
	 * action   : DELETE
	 * path     : /user/{id}
	 * desc     : Delete a user only if the token in header matches the token in db.
	 */
	@DeleteMapping("{id}")
    public void deleteUser(
    		@PathVariable Integer id,
    		@RequestHeader(value="token", defaultValue="") String token) throws Exception {
		
		// perform token validation before calling services
		if (userService.validateToken(id, token) == Boolean.FALSE) {
			throw new UnauthorizedException(INVALID_TOKEN + id);
		}
		
		try {
        	userService.deleteUser(id); 
		} catch (UserNotFoundException e) {
			throw e;
		}
		catch (Exception e) {
			throw e;
		}
    }

}
