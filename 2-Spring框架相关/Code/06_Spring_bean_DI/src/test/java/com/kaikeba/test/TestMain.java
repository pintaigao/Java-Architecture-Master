package com.kaikeba.test;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kaikeba.beans.Student;
import com.kaikeba.beans.Teacher;
import com.kaikeba.service.BaseService;

public class TestMain {

	public static void main(String[] args) {
		
		ApplicationContext factory = new ClassPathXmlApplicationContext("spring_config.xml");
		Teacher teacher = (Teacher) factory.getBean("teacher");
		System.out.println(teacher.getTeacherName());
		System.out.println(teacher.getFriendArray());
		System.out.println(teacher.getSchool());

	}

}
