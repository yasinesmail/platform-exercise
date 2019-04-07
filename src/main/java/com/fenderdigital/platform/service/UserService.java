package com.fenderdigital.platform.service;

import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenderdigital.platform.exception.UserAlreadyExistsException;
import com.fenderdigital.platform.exception.UserNotFoundException;
import com.fenderdigital.platform.model.User;
import com.fenderdigital.platform.repository.UserRepository;

@Service
public class UserService {
	
	private final String UserExistsErrorMsg = "User already exists : ";
	private final String UserNotExistsErrorMsg = "User not found : ";
	
	@Autowired
	private UserRepository userRepository;
	
	private User getUser(Integer id) throws UserNotFoundException {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("User " + id + " does not exists.");
		}
		return user.get();	
	}
	
	
	public Optional<User> findUserById(Integer id) {
		return userRepository.findById(id);
	}
	
	
	/*
	 * Function generates a new token based on the users email and today date.
	 */
	private String generateToken(User user) throws Exception {
		String token = user.getEmail() + Calendar.getInstance().getTime().toString();
		byte[] bytesOfMessage = token.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytesOfMessage);
		
		StringBuilder sb = new StringBuilder();
        for (byte b : thedigest) {
            sb.append(String.format("%02x", b));
        }
		
		return sb.toString();
	}
	
	
	/*
	 * Function takes a user id and token, and finds if 
	 * they match, if not then returns false.
	 */
	public Boolean validateToken(Integer id, String token) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) return Boolean.FALSE;
		if (!user.get().getToken().equals(token)) return Boolean.FALSE;
		return Boolean.TRUE;
	}
	
	
	public Optional<User> findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	/*
	 * Function adds new user to the database, however, it verifies that the email is not 
	 * already present in the database.
	 */
	public User addUser(User userDao) throws Exception {
		Optional<User> existingUser = userRepository.findByEmail(userDao.getEmail());
		if (existingUser.isPresent()) {
			throw new UserAlreadyExistsException(UserExistsErrorMsg + userDao.getEmail());
		}
		
		userRepository.save(userDao);
		return userDao;
	}
	
	
	/*
	 * Function returns a token for a user if there already exists one. however, if the
	 * user has logged out, then this function adds new token to database.
	 */
	public User getToken(User userDao) throws Exception {
		Optional<User> existingUser = userRepository.findByEmailAndPassword(userDao.getEmail(), userDao.getPassword());
		if (!existingUser.isPresent()) {
			throw new UserNotFoundException(UserNotExistsErrorMsg + userDao.getEmail());
		}
		
		if (existingUser.get().getToken() == null || existingUser.get().getToken().isEmpty()) {
			
			String token = generateToken(existingUser.get());
			existingUser.get().setToken(token);
			userRepository.save(existingUser.get());
			existingUser = userRepository.findById(existingUser.get().getId());
		}
		
		return existingUser.get();
	}
	
	
	/*
	 * Function sets the token to null
	 */
	public User updateToken(Integer id) {
		User user = getUser(id);
		user.setToken(null);
		userRepository.save(user);
		return user;
	}
	
	
	/*
	 * Function allows to update fields such as name, email and password in database.
	 */
	public User updateUser(User userDao) throws Exception {
		User user = getUser(userDao.getId());
		if (userDao.getName() != null) {
			user.setName(userDao.getName());
		}
		
		if (userDao.getEmail() != null && !userDao.getEmail().isEmpty()) {
			user.setEmail(userDao.getEmail());
		}
		
		if (userDao.getPassword() != null && !userDao.getPassword().isEmpty()) {
			user.setPassword(userDao.getPassword());
		}
		userRepository.save(user);
		return user;
	}

	
	/*
	 * Function deletes the user from the database.
	 */
	public void deleteUser(Integer id) throws Exception {
		User user = getUser(id);
		userRepository.delete(user);
		return;
	}

}
