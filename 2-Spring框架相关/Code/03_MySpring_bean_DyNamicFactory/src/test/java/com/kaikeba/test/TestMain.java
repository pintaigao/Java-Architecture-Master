package com.kaikeba.test;

import java.util.ArrayList;
import java.util.List;

import com.kaikeba.beans.Teacher;
import com.kaikeba.util.BeanDefined;
import com.kaikeba.util.BeanFactory;

public class TestMain {

	public static void main(String[] args) throws Exception {
		
		  //1.声明注册bean
		
		  
		  BeanDefined beanObj = new BeanDefined();
		  beanObj.setBeanId("teacher");
		  beanObj.setClassPath("com.kaikeba.beans.Teacher");
		  beanObj.setFactoryBean("factory1");
		  beanObj.setFactoryMethod("createTeacher");
		  beanObj.setScope("prototype");
		  
		  BeanDefined beanObj2 = new BeanDefined();
		  beanObj2.setBeanId("factory1");
		  beanObj2.setClassPath("com.kaikeba.beans.TeacherFactory");
		  
		  List configuration = new ArrayList();
		  configuration.add(beanObj);//spring核心配置
		  configuration.add(beanObj2);
		  
		  //2.声明一个Spring提供BeanFacotory
		  BeanFactory factory = new BeanFactory(configuration);
		 
		  
		  //3.开发人员向BeanFactory索要实例对象.
		  Teacher t= (Teacher) factory.getBean("teacher");
		  System.out.println("t="+t);
		 

	}

}
