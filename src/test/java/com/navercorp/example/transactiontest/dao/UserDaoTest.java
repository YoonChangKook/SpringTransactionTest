package com.navercorp.example.transactiontest.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.navercorp.example.transactiontest.config.DatabaseConfig;
import com.navercorp.example.transactiontest.dto.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfig.class})
public class UserDaoTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	@Autowired
	private UserDao userDao;

	@Test
	public void selectAllUsersTest() {
		List<User> users = userDao.selectAllUsers();

		LOGGER.debug(users.toString());
		assertNotNull(users);
	}

	@Test
	public void insertAndSelectTest() {
		User newUser = new User();
		newUser.setId(4);
		newUser.setName("test-user4");
		newUser.setEmail("test-email4@navercorp.com");

		userDao.insertUser(newUser);
		LOGGER.debug("new user id: {}", newUser.getId());

		User selectUser = userDao.selectUser(newUser.getId());

		LOGGER.debug(selectUser.toString());
		assertEquals(newUser.getId(), selectUser.getId());
		assertEquals(newUser.getName(), selectUser.getName());
		assertEquals(newUser.getEmail(), selectUser.getEmail());
	}
}
