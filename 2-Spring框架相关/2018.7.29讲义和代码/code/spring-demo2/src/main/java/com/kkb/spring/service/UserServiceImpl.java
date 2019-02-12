package com.kkb.spring.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

//在需要被spring IoC容器管理的类上面加上组件注解
//value：指定该类在容器中的唯一标识，相当于bean标签的id
//@Component(value="userService")
//默认的bean的id就是类名首字母小写，比如这个类的bean的id是userServiceImpl
//@Component
//@Service
@Controller
public class UserServiceImpl implements UserService {

	@Autowired(required=false)
	@Qualifier("date")
	// @Resource
	private Date date;

	//简单类型的注入（配合properties文件使用）
	@Value("${id}")
	private int id;
	
	@Override
	public void saveUser() {
		System.out.println("IoC 演示 之UserService" +"  :  "+id);
	}

}
