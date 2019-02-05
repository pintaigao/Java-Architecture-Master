package com.kaikeba.test;

import java.util.ArrayList;
import java.util.List;

import com.kaikeba.beans.Teacher;
import com.kaikeba.service.BaseService;
import com.kaikeba.util.BeanDefined;
import com.kaikeba.util.BeanFactory;

public class TestMain {

	public static void main(String[] args) throws Exception {
		
		  //1.声明注册bean
		
		  
		  BeanDefined beanObj = new BeanDefined();
		  beanObj.setBeanId("isomeService");
		  beanObj.setClassPath("com.kaikeba.serviceImpl.ISomeService");
		 
		  
		  BeanDefined beanObj2 = new BeanDefined();		
		  beanObj2.setClassPath("com.kaikeba.util.MyBeanPostProcessor");
		  
		  List configuration = new ArrayList();
		  configuration.add(beanObj);//spring核心配置
		  configuration.add(beanObj2);
		  
		  //2.声明一个Spring提供BeanFacotory
		  BeanFactory factory = new BeanFactory(configuration);
		 
		  
		  //3.开发人员向BeanFactory索要实例对象.
		  BaseService t= (BaseService) factory.getBean("isomeService");
		  System.out.println("t="+t);
		  System.out.println(t.doSome());//HELLO MIKE
		 

	}

}
