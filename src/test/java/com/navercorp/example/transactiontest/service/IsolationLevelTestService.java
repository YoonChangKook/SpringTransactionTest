package com.navercorp.example.transactiontest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.navercorp.example.transactiontest.dao.UserDao;
import com.navercorp.example.transactiontest.dto.User;

@Service("isolationLevelTestService")
public class IsolationLevelTestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IsolationLevelTestService.class);

	private final UserService userService;
	private final UserDao userDao;

	/**
	 * @param userService 트랜잭션 전파 방식을 REQUIRES_NEW로 사용하여, Transactional 메서드 내에서 별개의 트랜잭션으로 동작하는 UserService 객체
	 */
	public IsolationLevelTestService(@Qualifier("propagationNewUserService") UserService userService, UserDao userDao) {
		this.userService = userService;
		this.userDao = userDao;
	}

	/**
	 * 격리수준이 READ_COMMITED인 상태에서 별개의 트랜잭션으로 업데이트 후 id로 다시 읽었을 때 결과가 다른지 반환하는 메서드
	 *
	 * @param id 업데이트 할 유저의 아이디
	 * @param name 유저 이름
	 * @param email 유저 이메일
	 * @return 업데이트 후 id로 다시 읽었을 때, 이전과 다른 결과인지
	 */
	// TODO: 격리수준이 READ_COMMITTED 임에도 불구하고 Non-Repeatable-Read 문제가 일어나지 않는 이유 알아보기
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public boolean nonRepeatableRead(int id, String name, String email) {
		User beforeUpdate = userService.selectUser(id);
		userService.updateUser(id, name, email);
		User afterUpdate =  userService.selectUser(id);

		LOGGER.debug("before update: {}", beforeUpdate);
		LOGGER.debug("after update: {}", afterUpdate);

		return !(beforeUpdate.getName().equals(afterUpdate.getName()) && beforeUpdate.getEmail().equals(afterUpdate.getEmail()));
	}

	/**
	 * 격리수준이 REPEATABLE_READ인 상태에서 별개의 트랜잭션으로 업데이트 후 id로 다시 읽었을 때 결과가 같은지 반환하는 메서드
	 *
	 * @param id 업데이트 할 유저의 아이디
	 * @param name 유저 이름
	 * @param email 유저 이메일
	 * @return 업데이트 후 id로 다시 읽었을 때, 이전과 같은 결과인지
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean repeatableRead(int id, String name, String email) {
		User beforeUpdate = userService.selectUser(id);
		userService.updateUser(id, name, email);
		User afterUpdate = userService.selectUser(id);

		LOGGER.debug("before update: {}", beforeUpdate);
		LOGGER.debug("after update: {}", afterUpdate);

		return beforeUpdate.getName().equals(afterUpdate.getName()) && beforeUpdate.getEmail().equals(afterUpdate.getEmail());
	}

	/**
	 * 격리수준이 REPEATABLE_READ인 상태에서 별개의 트랜잭션으로 삽입한 결과가 현재 트랜잭션에서 읽히는지 (phantom-read) 테스트
	 *
	 * @param name 삽입할 유저의 이름
	 * @param email 삽입할 유저의 이메일
	 * @return phantom-read가 일어나는지 여부
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public boolean phantomRead(String name, String email) {
		List<User> beforeInsertUsers = userService.selectAllUsers();

		// 별도의 트랜잭션으로 insert
		User insertUser = userService.insertUser(name, email);

		// 현재 트랜잭션에서 update. 현재 트랜잭션의 snapshot에는 insert한 유저의 정보가 없지만 DML 구문 실행 시 snapshot에 반영된다.
		userDao.updateUser(insertUser.getId(), "TEST", "TEST");

		List<User> afterInsertUsers = userService.selectAllUsers();

		// 읽은 개수가 다르다면 phantom-read가 발생한 것이다.
		return beforeInsertUsers.size() < afterInsertUsers.size();
	}
}
