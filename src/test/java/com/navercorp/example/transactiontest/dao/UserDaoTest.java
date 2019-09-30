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
	private static final int SELECT_TEST_ID = 1;
	private static final String SELECT_TEST_NAME = "test-user1";
	private static final String SELECT_TEST_EMAIL = "test-email1@navercorp.com";
	private static final int UPDATE_TEST_ID = 2;
	private static final String UPDATE_TEST_NAME = "update-test-user";
	private static final String UPDATE_TEST_EMAIL = "update-test-email@navercorp.com";

	@Autowired
	private UserDao userDao;

	@Test
	public void selectUserTest() {
		User user = userDao.selectUser(SELECT_TEST_ID);

		LOGGER.debug("id: {}, name: {}, email: {}", user.getId(), user.getName(), user.getEmail());
		assertEquals(user.getId(), SELECT_TEST_ID);
		assertEquals(user.getName(), SELECT_TEST_NAME);
		assertEquals(user.getEmail(), SELECT_TEST_EMAIL);
	}

	@Test
	public void selectAllUsersTest() {
		List<User> users = userDao.selectAllUsers();

		LOGGER.debug(users.toString());
		assertNotNull(users);
	}

	@Test
	public void insertAndSelectTest() {
		User newUser = new User();
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

	@Test
	public void updateAndSelectTest() {
		userDao.updateUser(UPDATE_TEST_ID, UPDATE_TEST_NAME, UPDATE_TEST_EMAIL);

		User selectUser = userDao.selectUser(UPDATE_TEST_ID);

		LOGGER.debug(selectUser.toString());
		assertEquals(UPDATE_TEST_NAME, selectUser.getName());
		assertEquals(UPDATE_TEST_EMAIL, selectUser.getEmail());
	}
}
