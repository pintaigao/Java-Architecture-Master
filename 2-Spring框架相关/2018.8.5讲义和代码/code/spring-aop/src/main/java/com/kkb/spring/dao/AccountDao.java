package com.kkb.spring.dao;

public interface AccountDao {

	void update(String name,double money);
	
	double queryMoney(String name);
}
