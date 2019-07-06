package com.hptg.springdemo.test;

import com.hptg.springdemo.service.UserService;
import com.hptg.springdemo.service.UserService2;
import com.hptg.springdemo.service.UserServiceImpl1;
import com.hptg.springdemo.service.UserServiceImpl6;
import com.hptg.springdemo.utils.MyProxyUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContextAOPAnnotation.xml")
public class TestAOPAnnotation {

    @Autowired
    private UserService2 userService2;

    // 基于纯注解的AOP测试
    @Test
    public void test2() {
        userService2.saveUser();
        System.out.println("=================");
        userService2.saveUser("lisi");
        System.out.println("=================");
        userService2.updateUser();
    }
}