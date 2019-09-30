package com.navercorp.example.transactiontest.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.navercorp.example.transactiontest.dao.UserDao;
import com.navercorp.example.transactiontest.dto.User;

/**
 * DML에 대해 트랜잭션 전파를 REQUIRES_NEW로 하여 별개의 트랜잭션으로 동작하도록 도와주는 메서드
 *
 * @author 국윤창
 */
@Service("propagationNewUserService")
public class PropagationNewUserService extends UserServiceImpl {
	public PropagationNewUserService(UserDao userDao) {
		super(userDao);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User insertUser(String name, String email) {
		return super.insertUser(name, email);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updateUser(int id, String name, String email) {
		return super.updateUser(id, name, email);
	}
}
