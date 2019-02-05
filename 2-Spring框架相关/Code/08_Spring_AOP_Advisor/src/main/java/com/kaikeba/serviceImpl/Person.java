package com.kaikeba.serviceImpl;

import com.kaikeba.service.BaseService;

public class Person implements BaseService {

	public void eat() {//切入点 PointCut  主要业务方法
		
           System.out.println("吃泡面");
	}

	public void wc() {//切入点 PointCut   主要业务方法
		
		 System.out.println("上厕所");
	}

}
