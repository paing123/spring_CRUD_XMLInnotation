package com.techfun.mvc.service;

import java.util.List;

import com.techfun.mvc.model.User;

public interface UserService {

	User findById(Integer id) throws Exception;
	
	List<User> findAll() throws Exception;

	void saveOrUpdate(User user) throws Exception;
	
	void delete(int id) throws Exception;
	
}