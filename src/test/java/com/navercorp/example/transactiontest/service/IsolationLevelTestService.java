package com.navercorp.example.transactiontest.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.navercorp.example.transactiontest.dto.User;

@Service("isolationLevelTestService")
public class IsolationLevelTestService {
	private final UserService userService;

	public IsolationLevelTestService(@Qualifier("propagationNewUserService") UserService userService) {
		this.userService = userService;
	}

	/**
	 * 고립성이 READ_COMMITED인 상태에서 업데이트 후 id로 다시 읽었을 때 결과를 반환하는 메서드
	 *
	 * @param id 업데이트 할 유저의 아이디
	 * @param name 유저 이름
	 * @param email 유저 이메일
	 * @return 업데이트 후 id로 다시 읽었을 때 결과
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public User nonRepeatableRead(int id, String name, String email) {
		userService.updateUser(id, name, email);

		return userService.selectUser(id);
	}

	/**
	 * 고립성이 REPEATABLE_READ인 상태에서 업데이트 후 id로 다시 읽었을 때 결과를 반환하는 메서드
	 *
	 * @param id 업데이트 할 유저의 아이디
	 * @param name 유저 이름
	 * @param email 유저 이메일
	 * @return 업데이트 후 id로 다시 읽었을 때 결과
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public User repeatableRead(int id, String name, String email) {
		userService.updateUser(id, name, email);

		return userService.selectUser(id);
	}
}
