package com.kaikeba.test;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kaikeba.beans.Student;
import com.kaikeba.beans.Teacher;
import com.kaikeba.service.BaseService;

public class TestMain {

	public static void main(String[] args) {
		
		ApplicationContext factory = new ClassPathXmlApplicationContext("spring_config.xml");
		BaseService personProxy = (BaseService) factory.getBean("personProxy");
		personProxy.eat();//洗手  吃饭
		personProxy.wc();// wc
	}

}
