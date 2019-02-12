package com.kkb.spring.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.kkb.spring.service.UserService;
import com.kkb.spring.service.UserServiceImpl;

/**
 * 主要作用就是生成代理类
 * 
 * @author think
 *
 */
public class MyProxyUtils {

	/**
	 * 使用JDK的动态代理实现 它是基于接口实现的
	 * 
	 * @param serviceImpl
	 * @return
	 */
	public static UserService getProxy(UserService service) {

		// Proxy是JDK中的API类
		// 第一个参数：目标对象的类加载器
		// 第二个参数：目标对象的接口
		// 第二个参数：代理对象的执行处理器
		UserService userService = (UserService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
				service.getClass().getInterfaces(), new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

						System.out.println("记录日志-开始");

						// 下面的代码，是反射中的API用法
						// 该行代码，实际调用的是目标对象的方法
						Object object = method.invoke(service, args);

						System.out.println("记录日志-结束");
						return object;
					}
				});

		return userService;
	}

	/**
	 * 使用CGLib动态代理技术实现 它是基于继承的方式实现的
	 * 
	 * @param service
	 * @return
	 */
	public static UserService getProxyByCgLib(UserService service) {

		// 创建增强器
		Enhancer enhancer = new Enhancer();
		// 设置需要增强的类的类对象
		enhancer.setSuperclass(UserServiceImpl.class);

		// 设置回调函数
		enhancer.setCallback(new MethodInterceptor() {

			// MethodProxy：代理之后的对象的方法引用
			@Override
			public Object intercept(Object object, Method method, Object[] arg, MethodProxy methodProxy)
					throws Throwable {

				long start = System.currentTimeMillis();
				System.out.println("记录程序开始时间..." + start);

				// 因为代理对象是目标对象的子类
				// 该行代码，实际调用的是目标对象的方法
				// object :代理对象
				Object object2 = methodProxy.invokeSuper(object, arg);

				long end = System.currentTimeMillis();
				System.out.println("记录程序结束时间..." + end);
				System.out.println("记录程序执行总时长..." + (end - start));
				return object2;
			}
		});

		// 获取增强之后的代理对象
		UserService userService = (UserService) enhancer.create();
		return userService;
	}
}
