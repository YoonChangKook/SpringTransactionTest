package com.navercorp.example.transactiontest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.navercorp.example.transactiontest.dto.User;

/**
 * 트랜잭션을 테스트하기 위한 서비스 클래스
 *
 * @author 국윤창
 */
@Service("transactionalProxyTestService")
public class TransactionalProxyTestService {
	private final UserService userService;

	public TransactionalProxyTestService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 테스트 하기 위해 users 테이블에 insert 후 예외를 발생시키는 메서드
	 *
	 * @param name 유저 이름
	 * @param email 유저 이메일
	 * @throws RuntimeException 테스트를 위해 insert 후 발생
	 */
	@Transactional
	public User insertUserWithTransaction(String name, String email) {
		this.userService.insertUser(name, email);

		throw new RuntimeException("Test Exception");
	}

	/**
	 * 트랜잭션을 붙이지 않은 상태에서, 내부의 트랜잭션 메서드를 직접 호출하는 update 메서드
	 *
	 * @param id 업데이트 할 유저 id
	 * @param name 유저 이름
	 * @param email 유저 이메일
	 * @throws RuntimeException 테스트를 위해 insert 후 발생
	 */
	public int updateUserWithoutTransaction(int id, String name, String email) {
		return transactionalUpdateUser(id, name, email);
	}

	@Transactional
	public int transactionalUpdateUser(int id, String name, String email) {
		this.userService.updateUser(id, name, email);

		throw new RuntimeException("Test Exception");
	}
}
