package com.hptg.springdemo.test;

import com.hptg.springdemo.configuration.SpringConfiguration;
import com.hptg.springdemo.service.UserService;
import com.hptg.springdemo.service.UserService2;
import com.hptg.springdemo.service.UserServiceImpl1;
import com.hptg.springdemo.service.UserServiceImpl2;
import com.hptg.springdemo.service.UserServiceImpl3;
import com.hptg.springdemo.service.UserServiceImpl6;
import com.hptg.springdemo.utils.MyProxyUtils;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {

    // Test1: IOC的基本使用方法
    @Test
    public void test1() {
        // 创建容器
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 根据Bean的类型，从容器中获取实例
        // P.s 若只有一个Class实现了UserService， 则可以直接（见下一行代码）
        // UserService service1 = context.getBean(UserService.class);
        UserService service1 = context.getBean(UserServiceImpl1.class);
        UserService service2 = (UserService) context.getBean("userService1");
        service1.saveUser();
        service2.saveUser();
    }

    // Test2: 基于XML的Component-Scan的例子
    @Test
    public void test2() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService service1 = context.getBean(UserServiceImpl2.class);
        UserService service2 = (UserService) context.getBean("UserServiceImpl2");
        service1.saveUser();
        service2.saveUser();
    }

    // Test3: 基于纯注解的案例
    @Test
    public void test3() {
        // 创建纯注解方式的spring容器
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        UserService service = (UserService) context.getBean("userService3");
        service.saveUser();
    }

    //Test4，对应第12章，AOP的开头，这里纯粹演示Proxy的使用方法，没有用到Spring Component Scan那一套
    @Test
    public void testJDKProxy() {

        //创建目标对象
        UserService service = new UserServiceImpl1();

        //生成代理对象
        UserService proxy = MyProxyUtils.getProxy(service);

        //调用目标对象的方法
        service.saveUser();

        System.out.println("===============");
        //调用代理对象的方法
        proxy.saveUser();
    }

    @Test
    public void testCgLibProxy() {

        //创建目标对象
        UserService service = new UserServiceImpl1();

        //生成代理对象
        UserService proxy = MyProxyUtils.getProxyByCgLib(service);

        //调用目标对象的方法
        service.saveUser();

        System.out.println("===============调用代理对象的方法");
        //调用代理对象的方法
        proxy.saveUser();
    }

    public static void main(String[] args) {
        TestSpring test = new TestSpring();
        test.test2();
    }
}
