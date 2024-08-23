package com.sahana.service;

import java.util.List;

import com.sahana.exception.UserException;
import com.sahana.modal.User;

public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public List<User> findAllUsers();

}
