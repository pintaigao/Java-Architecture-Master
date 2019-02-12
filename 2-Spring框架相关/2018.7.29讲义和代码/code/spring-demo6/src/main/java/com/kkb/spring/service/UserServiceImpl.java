package com.kkb.spring.service;

/**
 * AOP中的目标对象
 * 
 * @author think
 *
 */
public class UserServiceImpl implements UserService {

	@Override
	public void saveUser() {
		System.out.println("添加用户");
		// 抛出异常的代码
		// System.out.println(1 / 0);
	}
	@Override
	public void saveUser(String name ) {
		System.out.println("添加用户 : name");
		// 抛出异常的代码
		// System.out.println(1 / 0);
	}

	@Override
	public void updateUser() {
		System.out.println("修改用户");
		// 抛出异常的代码
		// System.out.println(1 / 0);
	}

}
