package com.navercorp.example.transactiontest.service;

import java.util.List;

import com.navercorp.example.transactiontest.dto.User;

/**
 * User 객체에 대한 비즈니스 로직을 담당하는 인터페이스
 *
 * @author 국윤창
 */
public interface UserService {
	User selectUser(int id);

	List<User> selectAllUsers();

	User insertUser(String name, String email);

	int updateUser(int id, String name, String email);
}
