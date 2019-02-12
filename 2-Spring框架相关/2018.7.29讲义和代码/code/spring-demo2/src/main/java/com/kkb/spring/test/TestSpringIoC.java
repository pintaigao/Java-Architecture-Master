package com.kkb.spring.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kkb.spring.service.UserService;

public class TestSpringIoC {

	@Test
	public void test1() {
		//创建容器
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		//根据Bean的类型，从容器中获取实例
		UserService service1 = context.getBean(UserService.class);
		UserService service2 = (UserService) context.getBean("userServiceImpl");
		
		service1.saveUser();
		service2.saveUser();
	}
}
