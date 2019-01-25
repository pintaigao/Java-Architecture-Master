package com.kaikeba.advice;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;
/*
 * 
 *   public class Agent implements InvocationHandler{
 *   
 *      private BaseService obj;//当前具体被监控对象
 *      
 *      public Agent(BasseSercie param){
 *          this.obj = param;
 *      }
 *   
 *      public Object invoke(Object proxy,Method method,Object[] args){
 *             //织入顺序
 *      }
 *      
 *      //次要业务
 *      public wash(){
 *      
 *      }
 *   }
 * 
 * */
public class MyBeforeAdvice implements MethodBeforeAdvice {

	//切面：次要业务
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		System.out.println("-----洗手-----");
        ProxyFactoryBean cc; 
	}

}
