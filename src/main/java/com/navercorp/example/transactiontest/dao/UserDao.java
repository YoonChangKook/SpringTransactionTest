package com.navercorp.example.transactiontest.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.navercorp.example.transactiontest.dto.User;

@Mapper
public interface UserDao {
	List<User> selectAllUsers();

	User selectUser(int id);

	int insertUser(User user);

	int updateUser(@Param("id") int id, @Param("name") String name, @Param("email") String email);
}
