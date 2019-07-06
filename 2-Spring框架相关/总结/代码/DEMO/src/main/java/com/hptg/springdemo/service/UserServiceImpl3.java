package com.hptg.springdemo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

/*方式二：@Controller("UserServiceImpl3") ==> 这个现在写在SpringConfiguration.java中的@Bean ===> @Controller 要配合写在SpringConfiguration中的@ComponentScan配合使用*/
//@PropertySource("classpath:data.properties") ===> 这个，顶上没有@Controller的时候是没用的！！！
public class UserServiceImpl3 implements UserService {

    // 简单类型的注入（配合properties文件使用）
    @Value("${id}")
    private String id;

    @Override
    public void saveUser() {
        System.out.println("IoC 演示 之UserService3 + id: " + id);
    }
}
