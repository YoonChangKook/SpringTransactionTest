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

/**
 * isolation 레벨에 따라서 발생하는 문제들을 테스트한다.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, DatabaseConfig.class})
public class IsolationLevelTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(IsolationLevelTest.class);
	private static final int NON_REPEATABLE_READ_TEST_ID = 1;
	private static final String NON_REPEATABLE_READ_TEST_NAME = "non-repeatable-read-test-user";
	private static final String NON_REPEATABLE_READ_TEST_EMAIL = "non-repeatable-read-test-user@navercorp.com";
	private static final int REPEATABLE_READ_TEST_ID = 2;
	private static final String REPEATABLE_READ_TEST_NAME = "repeatable-read-test-user";
	private static final String REPEATABLE_READ_TEST_EMAIL = "repeatable-read-test-user@navercorp.com";

	@Autowired
	private IsolationLevelTestService isolationLevelTestService;

	@Autowired
	private UserDao userDao;

	/**
	 * 격리수준이 READ_COMMITED 일 때, 다른 트랜잭션에서 커밋된 내용이 select에 반영이 되는지 테스트 (NON_REPEATABLE_READ)
	 */
	@Test
	public void nonRepeatableReadTest() {
		boolean nonRepeatableReadTest = isolationLevelTestService.nonRepeatableRead(NON_REPEATABLE_READ_TEST_ID, NON_REPEATABLE_READ_TEST_NAME, NON_REPEATABLE_READ_TEST_EMAIL);

		LOGGER.debug("outside transaction: {}", userDao.selectUser(NON_REPEATABLE_READ_TEST_ID));
		// 아래 결과가 참이면 non-repeatable read 발생
		assertTrue(nonRepeatableReadTest);
	}

	/**
	 * 격리수준이 REPEATABLE_READ 일 때, 다른 트랜잭션에서 커밋된 내용이 select에 반영이 안 되는지 테스트
	 */
	@Test
	public void repeatableReadTest() {
		boolean repeatableReadTest = isolationLevelTestService.repeatableRead(REPEATABLE_READ_TEST_ID, REPEATABLE_READ_TEST_NAME, REPEATABLE_READ_TEST_EMAIL);

		LOGGER.debug("outside transaction: {}", userDao.selectUser(REPEATABLE_READ_TEST_ID));
		// 아래 결과가 참이면 non-repeatable read 발생 안함
		assertTrue(repeatableReadTest);
	}
}
