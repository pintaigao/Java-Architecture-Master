package com.kkb.spring.test;

import org.junit.Test;

import com.kkb.spring.service.UserService;
import com.kkb.spring.service.UserServiceImpl;
import com.kkb.spring.utils.MyProxyUtils;

public class TestProxy {

	@Test
	public void testJDKProxy() {
		
		//创建目标对象
		UserService service = new UserServiceImpl();
		
		//生成代理对象
		UserService proxy = MyProxyUtils.getProxy(service);
		
		//调用目标对象的方法
		service.saveUser();
		
		System.out.println("===============");
		//调用代理对象的方法
		proxy.saveUser();
	}
	@Test
	public void testCgLibProxy() {
		
		//创建目标对象
		UserService service = new UserServiceImpl();
		
		//生成代理对象
		UserService proxy = MyProxyUtils.getProxyByCgLib(service);
		
		//调用目标对象的方法
		service.saveUser();
		
		System.out.println("===============");
		//调用代理对象的方法
		proxy.saveUser();
	}
}
