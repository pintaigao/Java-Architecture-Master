package com.kaikeba.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.kaikeba.serviceImpl.ISomeService;

public class MyBeanPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("bean对象初始化之前。。。。。");
		return bean;
		//return  bean对象监控代理对象
	}

	public Object postProcessAfterInitialization(final Object beanInstance, String beanName) throws BeansException {
		// 为当前bean对象注册代理监控对象，负责增强bean对象方法能力
		Class beanClass = beanInstance.getClass();
		if (beanClass == ISomeService.class) {
			Object proxy = Proxy.newProxyInstance(beanInstance.getClass().getClassLoader(),
					beanInstance.getClass().getInterfaces(), new InvocationHandler() {
						/*
						 * 
						 * method:doSome args:doSome执行接受实参 proxy:代理监控对对象
						 **/
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							System.out.println("ISomeService doSome 被拦截");
							String result = (String) method.invoke(beanInstance, args);// beanInstance.doSome
							return result.toUpperCase();
						}

					});
			return proxy;
		}
		return beanInstance;
	}
	
	

}
