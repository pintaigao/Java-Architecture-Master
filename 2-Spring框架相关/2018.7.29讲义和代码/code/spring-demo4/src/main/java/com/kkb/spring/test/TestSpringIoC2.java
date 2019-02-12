package com.kkb.spring.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kkb.spring.configuration.SpringConfiguration;
import com.kkb.spring.service.UserService;

//@RunWith：Junit自身的注解，它的作用是可以指定一个新的运行器，进行单元测试
//SpringJUnit4ClassRunner:Spring提供的单元测试运行器
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration:SpringJUnit4ClassRunner运行器需要的上下文配置信息，方便创建Spring容器
//classes：纯注解方式时，读取配置类
//locations：XML方式时，读取配置文件
@ContextConfiguration(classes=SpringConfiguration.class)
//@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestSpringIoC2 {

	@Resource
	private UserService userService;

	@Test
	public void test1() {
		userService.saveUser();
	}
}
