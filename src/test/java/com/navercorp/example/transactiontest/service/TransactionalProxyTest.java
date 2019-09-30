package com.navercorp.example.transactiontest.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.navercorp.example.transactiontest.config.AppConfig;
import com.navercorp.example.transactiontest.config.DatabaseConfig;
import com.navercorp.example.transactiontest.dao.UserDao;
import com.navercorp.example.transactiontest.dto.User;

/**
 * Transactional 애노테이션이 붙은 메서드를 내부에서 호출했을 때 제대로 호출되는지 테스트
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
public class TransactionalProxyTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionalProxyTest.class);
	private static final int UPDATE_TEST_ID = 1;
	private static final String UPDATE_TEST_NAME = "update-test-user";
	private static final String UPDATE_TEST_EMAIL = "update-test-user@navercorp.com";
	private static final String INSERT_TEST_NAME = "insert-test-user";
	private static final String INSERT_TEST_EMAIL = "insert-test-user@navercorp.com";

	/**
	 * Transactional을 테스트할 프록시 객체를 주입받는다.
	 */
	@Autowired
	private TransactionalProxyTestService transactionalProxyTestService;

	@Autowired
	private UserDao userDao;

	/**
	 * Transactional 메서드를 호출하여 RuntimeException이 발생했을 때 insert 결과가 롤백됐는지 확인하는 테스트
	 */
	@Test
	public void rollbackFailTest() {
		User beforeUpdateUser = userDao.selectUser(UPDATE_TEST_ID);

		try {
			transactionalProxyTestService.updateUserWithoutTransaction(UPDATE_TEST_ID, UPDATE_TEST_NAME, UPDATE_TEST_EMAIL);
		} catch (RuntimeException testEx) {
			LOGGER.debug(testEx.toString());
		}

		User afterUpdateUser = userDao.selectUser(UPDATE_TEST_ID);
		// 롤백이 제대로 안 됐다면 아래 결과는 참이다.
		assertNotEquals(beforeUpdateUser.getName(), afterUpdateUser.getName());
		assertNotEquals(beforeUpdateUser.getEmail(), afterUpdateUser.getEmail());
	}

	/**
	 * Transactional 메서드를 프록시 객체 내부에서 직접 호출했을 때, RuntimeException 발생 후 insert 결과가 롤백됐는지 확인하는 테스트
	 */
	@Test
	public void rollbackTest() {
		List<User> beforeInsertUsers = userDao.selectAllUsers();

		try {
			transactionalProxyTestService.insertUserWithTransaction(INSERT_TEST_NAME, INSERT_TEST_EMAIL);
		} catch (RuntimeException testEx) {
			LOGGER.debug(testEx.toString());
		}

		List<User> afterInsertUsers = userDao.selectAllUsers();
		// 롤백이 제대로 됐다면 아래 결과는 참이다.
		assertEquals(beforeInsertUsers.size(), afterInsertUsers.size());
	}
}
