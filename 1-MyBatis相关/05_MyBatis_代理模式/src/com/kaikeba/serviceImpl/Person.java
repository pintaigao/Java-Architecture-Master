package com.kaikeba.serviceImpl;

import com.kaikeba.service.BaseService;

public class Person implements BaseService {

	@Override
	public void eat() {//主要业务，代理模式要求开发人员只关心主要业务
	
        System.out.println("使用筷子吃饭....");
	}

	@Override
	public void wc() {
		   System.out.println("测试地球重力是否存在");

	}

}
