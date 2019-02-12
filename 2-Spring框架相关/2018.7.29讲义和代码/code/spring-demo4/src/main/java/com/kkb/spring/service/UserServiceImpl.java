package com.kkb.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

//如果不指定bean的id，默认bean的id是类名的首字母小写，也就是userServiceImpl
@Service(value="userService")
@PropertySource("classpath:data.properties")
//相当于context:property-placeholder标签
public class UserServiceImpl implements UserService {

	@Value("${id}")
	private int id;
	
	@Override
	public void saveUser() {
		System.out.println("IoC 演示 之UserService" +"  :  " + id);
	}

}
