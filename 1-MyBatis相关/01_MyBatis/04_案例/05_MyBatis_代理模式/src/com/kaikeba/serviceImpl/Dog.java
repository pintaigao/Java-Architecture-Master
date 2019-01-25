package com.kaikeba.serviceImpl;

import com.kaikeba.service.BaseService;

public class Dog implements BaseService {

	@Override
	public void eat() {
	   System.out.println("啃骨头");
	}

	@Override
	public void wc() {
		 System.out.println("三腿立");

	}

}
