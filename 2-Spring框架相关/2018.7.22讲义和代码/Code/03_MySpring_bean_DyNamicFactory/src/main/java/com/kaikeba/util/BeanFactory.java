package com.kaikeba.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanFactory {
	
	   private List<BeanDefined> beanDefinedList;
	   private Map<String ,Object> SpringIoc;//已经创建好实例对象
	   

	public List<BeanDefined> getBeanDefinedList() {
		return beanDefinedList;
	}

	
	
	
	public BeanFactory(List<BeanDefined> beanDefinedList) throws Exception {
		
		this.beanDefinedList = beanDefinedList;
		SpringIoc  = new HashMap(); //所有scope="singleton" 采用单类模式管理bean对象
		for(BeanDefined beanObj:this.beanDefinedList){
			if("singleton".equals(beanObj.getScope())){
				Class classFile= Class.forName(beanObj.getClassPath());
				Object instance= classFile.newInstance();
				SpringIoc.put(beanObj.getBeanId(), instance);
			}
		}
		
	}




	public void setBeanDefinedList(List<BeanDefined> beanDefinedList) {
		this.beanDefinedList = beanDefinedList;
	}

	public Object getBean(String beanId) throws Exception {
		Object instance = null;
		for (BeanDefined beanObj : beanDefinedList) {
			if (beanId.equals(beanObj.getBeanId())) {
				String classPath = beanObj.getClassPath();
				Class classFile = Class.forName(classPath);
				String scope = beanObj.getScope();
				String factoryBean = beanObj.getFactoryBean();
				String factoryMehtod = beanObj.getFactoryMethod();
				if ("prototype".equals(scope)) {//.getBean每次都要返回一个全新实例对象

					if (factoryBean != null && factoryMehtod != null) {//用户希望使用指定工厂创建实例对象
						Object factoryObj = SpringIoc.get(factoryBean);
						Class factoryClass = factoryObj.getClass();
						Method methodObj = factoryClass.getDeclaredMethod(factoryMehtod, null);
						methodObj.setAccessible(true);
						instance = methodObj.invoke(factoryObj, null);
					} else {
						instance = classFile.newInstance();
					}
				} else {
					instance = SpringIoc.get(beanId);
				}
				return instance;
			}
		}
		return null;
	}
	   

}
