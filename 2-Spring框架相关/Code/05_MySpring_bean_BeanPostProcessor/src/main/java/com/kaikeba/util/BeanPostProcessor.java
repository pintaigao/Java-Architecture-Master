package com.kaikeba.util;



/*
 * @author:laozhao
 */
public interface BeanPostProcessor {
	
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
		return bean;
	}
	
	default Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
		return bean;
	}
}
