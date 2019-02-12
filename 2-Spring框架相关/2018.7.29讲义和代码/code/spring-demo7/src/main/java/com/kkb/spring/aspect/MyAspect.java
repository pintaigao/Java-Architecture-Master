package com.kkb.spring.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

//@Aspect:标记该类是一个切面类
@Aspect
// 需要将切面类交给Spring IoC容器管理
@Component("myAspect")
public class MyAspect {
	//切入点表达式
	private static final String str="execution(* *..*.*ServiceImpl.*(..))";

	// @Before：定义该方法是一个前置通知
	/*@Before(value = "execution(* *..*.*ServiceImpl.*(..))")
	public void before() {
		System.out.println("注解前置通知");
	}

	@After(value = "execution(* *..*.*ServiceImpl.*(..))")
	public void after() {
		System.out.println("注解最终通知");
	}*/
	
	@Before(value = "fn()")
	public void before() {
		System.out.println("注解前置通知");
	}

	@After(value = "fn()")
	public void after() {
		System.out.println("注解最终通知");
	}
	
	//使用PointCut注解，来定义一个通用切入点表达式
	@Pointcut(value=str)
	public void fn() {}
	
	
}
