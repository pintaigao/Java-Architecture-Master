package com.kaikeba.util;

public class BeanDefined {
	/*
	 * 
	 *   
	 *   <bean id  ,class,  scope.factory-bean,factory-method>
	 **/
	private String beanId;
	private String classPath;
	private String scope ="singleton";
	private String factoryBean=null;
	private String factoryMethod=null;
	
	
	
	
	public String getFactoryBean() {
		return factoryBean;
	}
	public void setFactoryBean(String factoryBean) {
		this.factoryBean = factoryBean;
	}
	public String getFactoryMethod() {
		return factoryMethod;
	}
	public void setFactoryMethod(String factoryMethod) {
		this.factoryMethod = factoryMethod;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	
	

}
