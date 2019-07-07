package com.hptg.springdemo.test;

import com.hptg.springdemo.service.UserService2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContextAOP.xml")
public class TestAOP {

    @Autowired
    private UserService2 userService2;

    @Test
    public void testBefore() {
        userService2.saveUser();
    }
}