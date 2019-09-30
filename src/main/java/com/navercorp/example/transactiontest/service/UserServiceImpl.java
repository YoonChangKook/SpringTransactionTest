package com.navercorp.example.transactiontest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.navercorp.example.transactiontest.dao.UserDao;
import com.navercorp.example.transactiontest.dto.User;

/**
 * UserService 구현 클래스
 *
 * @author 국윤창
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	private final UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User selectUser(int id) {
		return userDao.selectUser(id);
	}

	@Override
	public List<User> selectAllUsers() {
		return userDao.selectAllUsers();
	}

	@Override
	public User insertUser(String name, String email) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);

		userDao.insertUser(user);

		return user;
	}

	@Override
	public int updateUser(int id, String name, String email) {
		return userDao.updateUser(id, name, email);
	}
}
