package com.navercorp.example.transactiontest.service;

import com.navercorp.example.transactiontest.dto.User;

/**
 * User 객체에 대한 비즈니스 로직을 담당하는 인터페이스
 *
 * @author 국윤창
 */
public interface UserService {
	User insertUser(String name, String email);
}
