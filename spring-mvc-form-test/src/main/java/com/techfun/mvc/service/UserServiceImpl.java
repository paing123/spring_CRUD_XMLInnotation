package com.techfun.mvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techfun.mvc.dao.UserDao;
import com.techfun.mvc.model.User;

@Service("userService")
public class UserServiceImpl implements UserService {

	UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) throws Exception{
		this.userDao = userDao;
	}

	public User findById(Integer id) throws Exception{
		return userDao.findById(id);
	}

	public List<User> findAll() throws Exception{
		return userDao.findAll();
	}

	public void saveOrUpdate(User user) throws Exception{

		if (user.getId() == null) {
			userDao.save(user);
		} else {
			userDao.update(user);
		}

	}

	public void delete(int id) throws Exception{
		userDao.delete(id);
	}

}