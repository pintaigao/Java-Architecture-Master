package com.hptg.springdemo.service;


public class UserServiceImpl1 implements UserService {

    //有参构造，默认的无参构造就没有了,所以要手动把无参构造添加上
    public UserServiceImpl1(int id) {
        System.out.println(id);
    }

    public UserServiceImpl1() {
        System.out.println("无参构造");
    }

    @Override
    public void saveUser() {
        System.out.println("IoC 演示 之UserService");
    }
}
