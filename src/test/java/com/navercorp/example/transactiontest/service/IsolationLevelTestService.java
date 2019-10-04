package com.navercorp.example.transactiontest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.navercorp.example.transactiontest.dto.User;

@Service("isolationLevelTestService")
public class IsolationLevelTestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(IsolationLevelTestService.class);

	private final UserService userService;

	/**
	 * @param userService 트랜잭션 전파 방식을 REQUIRES_NEW로 사용하여, Transactional 메서드 내에서 별개의 트랜잭션으로 동작하는 UserService 객체
	 */
	public IsolationLevelTestService(@Qualifier("propagationNewUserService") UserService userService) {
		this.userService = userService;
	}

	/**
	 * 고립성이 READ_COMMITED인 상태에서 업데이트 후 id로 다시 읽었을 때 결과가 다른지 반환하는 메서드
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
	 * 고립성이 REPEATABLE_READ인 상태에서 업데이트 후 id로 다시 읽었을 때 결과가 같은지 반환하는 메서드
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
}
