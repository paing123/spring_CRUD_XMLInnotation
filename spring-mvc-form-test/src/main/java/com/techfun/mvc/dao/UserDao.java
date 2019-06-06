package com.techfun.mvc.dao;

import java.util.List;

import com.techfun.mvc.model.User;

public interface UserDao {

	User findById(Integer id) throws Exception;

	List<User> findAll() throws Exception;

	void save(User user) throws Exception;

	void update(User user) throws Exception;

	void delete(Integer id) throws Exception;
}