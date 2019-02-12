package com.kkb.spring.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.kkb.spring.configuration.SpringConfiguration;
import com.kkb.spring.service.UserService;

public class TestSpringIoC {

	private UserService userService;
	
	//执行@Test方法之前调用该方法
	@Before
	public void init() {
		// 创建纯注解方式的spring容器
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

		// 从容器中获取Bean的实例
		userService = context.getBean(UserService.class);
	}

	@Test
	public void test1() {
		userService.saveUser();
	}
}
