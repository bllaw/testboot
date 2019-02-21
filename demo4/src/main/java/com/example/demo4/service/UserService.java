package com.example.demo4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo4.dao.KugcMapper;
import com.example.demo4.dao.UserMapper;
import com.example.demo4.domain.Kugc;
import com.example.demo4.domain.User;

@Component
public class UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private KugcMapper kugcMapper;

	@Transactional
	public Kugc findKugcById(String id) {
		return kugcMapper.findKugcById(id);
	}

	@Transactional
	public User findUserById(int id) {
		final User user = userMapper.findUserById(id);
		if (user == null) {
			final User user2 = new User();
			user2.setId(id);
			user2.setName("auto " + id);
			user2.setEmail(id + "email@gmail.com");
			userMapper.insertUser(user2);

			final User user3 = new User();
			user3.setId(id + 1);
			user3.setName("auto " + (id + 1));
			user3.setEmail((id + 1) + "email@gmail.com");
			userMapper.insertUser(user3);
			return user2;
		} else {

			return user;
		}
	}
}
