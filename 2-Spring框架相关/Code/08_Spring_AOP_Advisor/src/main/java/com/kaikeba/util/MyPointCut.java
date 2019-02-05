package com.kaikeba.util;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

public class MyPointCut implements Pointcut {
	
	
	/*
	 * InvocationHandler接口
	 *    invoke(){
	 *        if(obj.getClass ！= person.class){
	 *              return
	 *        }
	 *        
	 *        if(!methodObj.getName.equals("eat")){
	 *               return 
	 *        }
	 *        //织入方式:次要业务方法和 Peson.eat()执行顺序
	 *        //前置通知
	 *          wash（）；
	 *          Person.eat()
	 *    }
	 * 
	 * */
	//使用依赖注入
	private ClassFilter classFilter;
	private MethodMatcher metodMatcher;

	public void setClassFilter(ClassFilter classFilter) {
		this.classFilter = classFilter;
	}

	public void setMetodMatcher(MethodMatcher metodMatcher) {
		this.metodMatcher = metodMatcher;
	}

	public ClassFilter getClassFilter() {
		// TODO Auto-generated method stub
		return this.classFilter;
	}

	public MethodMatcher getMethodMatcher() {
		// TODO Auto-generated method stub
		return this.metodMatcher;
	}

}
