package com.kkb.spring.service;

public class UserServiceImpl implements UserService {

    //有参构造，默认的无参构造就没有了,所以要手动把无参构造添加上
    public UserServiceImpl(int id) {
        System.out.println(id);
    }

    public UserServiceImpl() {
        System.out.println("无参构造");
    }

    @Override
    public void saveUser() {
        System.out.println("IoC 演示 之UserService");
    }

}
