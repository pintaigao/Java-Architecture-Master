package com.kkb.spring.advice;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 编写通知类
 * @author think
 *
 */
public class MyAdvice {
	
	//演示前置通知
	public void log() {
		System.out.println("开始记录日志了...");
	}
	
	//演示后置通知
	public void log2() {
		System.out.println("开始记录日志了log2...");
	}
	//演示最终通知
	public void log3() {
		System.out.println("开始记录日志了log3...");
	}
	//演示异常抛出通知
	public void log4() {
		System.out.println("开始记录日志了log4...");
	}
	/**
	 * 环绕通知
	 * 场景使用：事务管理
	 * @param joinPoint
	 * @throws Throwable
	 */
	public void log5(ProceedingJoinPoint joinPoint)  {
		System.out.println("前置通知");
		//调用目标对象的方法
		try {
			joinPoint.proceed();
			System.out.println("后置通知");
		} catch (Throwable e) {
			//相当于实现异常通知
			System.out.println("异常抛出配置");
			e.printStackTrace();
		}finally {
			System.out.println("最终通知");
		}
	}
}
