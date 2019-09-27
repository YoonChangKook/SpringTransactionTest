package com.navercorp.example.transactiontest.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.navercorp.example.transactiontest.config.AppConfig;
import com.navercorp.example.transactiontest.config.DatabaseConfig;
import com.navercorp.example.transactiontest.dao.UserDao;
import com.navercorp.example.transactiontest.dto.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
public class UserServiceProxyTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceProxyTest.class);

	/**
	 * Transactional을 테스트할 프록시 객체를 주입받는다.
	 */
	@Autowired
	@Qualifier("userServiceProxy")
	private UserService userService;

	@Autowired
	private UserDao userDao;

	/**
	 * Transactional 메서드를 호출하여 RuntimeException이 발생했을 때 insert 결과가 롤백됐는지 확인하는 테스트
	 */
	@Test
	public void rollbackTest() {
		List<User> beforeInsertUsers = userDao.selectAllUsers();

		try {
			((UserServiceProxy)userService).insertUserInnerCall("user-service-test", "user-service-test@navercorp.com");
		} catch (RuntimeException testEx) {
			LOGGER.debug(testEx.toString());
		}

		List<User> afterInsertUsers = userDao.selectAllUsers();
		// 롤백이 제대로 됐다면 아래 결과는 참이다.
		assertEquals(beforeInsertUsers.size(), afterInsertUsers.size());
	}

	/**
	 * Transactional 메서드를 프록시 객체 내부에서 직접 호출했을 때, RuntimeException 발생 후 insert 결과가 롤백됐는지 확인하는 테스트
	 */
	@Test
	public void InnerCallRollbackTest() {
		List<User> beforeInsertUsers = userDao.selectAllUsers();

		try {
			userService.insertUser("user-service-test", "user-service-test@navercorp.com");
		} catch (RuntimeException testEx) {
			LOGGER.debug(testEx.toString());
		}

		List<User> afterInsertUsers = userDao.selectAllUsers();
		// 롤백이 제대로 안 됐다면 아래 결과는 참이다.
		assertNotEquals(beforeInsertUsers.size(), afterInsertUsers.size());
	}
}
