package com.example.demo4.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.example.demo4.domain.User;

public interface UserMapper

{

	@Insert("insert into users(id,name,email) values(#{id},#{name},#{email})")
	void insertUser(User user);

	@Select("select id, name, email from users WHERE id=#{id}")
	User findUserById(Integer id);

	@Select("select id, name, email from users")
	List<User> findAllUsers();

}