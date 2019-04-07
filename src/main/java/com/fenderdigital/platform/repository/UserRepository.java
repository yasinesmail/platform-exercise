package com.fenderdigital.platform.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.fenderdigital.platform.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByEmailAndPassword(String email, String password);
}
