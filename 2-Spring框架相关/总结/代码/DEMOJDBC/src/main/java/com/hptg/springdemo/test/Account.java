package com.hptg.springdemo.test;

public class Account {

	
	private int id;
	private String tutor_name;
	
//	private double money;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return tutor_name;
	}

	public void setName(String name) {
		this.tutor_name = name;
	}

//	public double getMoney() {
//		return money;
//	}
//
//	public void setMoney(double money) {
//		this.money = money;
//	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", name=" + tutor_name +  "]";
	}
	
	
}
