package com.navercorp.example.transactiontest.service;

import static org.junit.Assert.*;

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
 * isolation 레벨에 따라서 발생하는 문제들을 테스트한다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
public class IsolationLevelTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(IsolationLevelTest.class);
	private static final int TEST_ID = 1;
	private static final String TEST_NAME = "isolation-test-user";
	private static final String TEST_EMAIL = "isolation-test-user@navercorp.com";

	/**
	 * Transactional을 테스트할 프록시 객체를 주입받는다.
	 */
	@Autowired
	private IsolationLevelTestService isolationLevelTestService;

	@Autowired
	private UserDao userDao;

	/**
	 * 고립성이 READ_COMMITED 일 때, 다른 트랜잭션에서 커밋된 내용이 select에 반영이 되는지 테스트 (NON_REPEATABLE_READ)
	 */
	@Test
	public void nonRepeatableReadTest() {
		User beforeUpdateUser = userDao.selectUser(TEST_ID);

		User updateUser = isolationLevelTestService.nonRepeatableRead(TEST_ID, TEST_NAME, TEST_EMAIL);

		// 아래 내용이 참이면
		assertNotEquals(beforeUpdateUser.getName(), updateUser.getName());
		assertNotEquals(beforeUpdateUser.getEmail(), updateUser.getEmail());
	}

	/**
	 * 고립성이 REPEATABLE_READ 일 때, 다른 트랜잭션에서 커밋된 내용이 select에 반영이 안 되는지 테스트
	 */
	@Test
	public void repeatableReadTest() {
		User beforeUpdateUser = userDao.selectUser(TEST_ID);

		User updateUser = isolationLevelTestService.repeatableRead(TEST_ID, TEST_NAME, TEST_EMAIL);

		assertEquals(beforeUpdateUser.getName(), updateUser.getName());
		assertEquals(beforeUpdateUser.getEmail(), updateUser.getEmail());
	}
}
