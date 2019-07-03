package com.hptg.springdemo.test;

import com.hptg.springdemo.service.UserService;
import com.hptg.springdemo.service.UserServiceImpl1;
import com.hptg.springdemo.service.UserServiceImpl2;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
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

    // Test2: 基于XML的Component Scan的例子
    @Test
    public void test2() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserService service1 = context.getBean(UserServiceImpl2.class);
        UserService service2 = (UserService) context.getBean("userServiceImpl2");
        service1.saveUser();
        service2.saveUser();
    }

    public static void main(String[] args) {
        TestSpring test = new TestSpring();
        test.test2();    
    }
}
