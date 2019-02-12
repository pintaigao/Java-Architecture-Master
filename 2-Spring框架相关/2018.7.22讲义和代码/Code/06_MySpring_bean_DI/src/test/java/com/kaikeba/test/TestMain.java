package com.kaikeba.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kaikeba.beans.Teacher;
import com.kaikeba.service.BaseService;
import com.kaikeba.util.BeanDefined;
import com.kaikeba.util.BeanFactory;

public class TestMain {

    public static void main(String[] args) throws Exception {

        //1.声明注册bean


        BeanDefined beanObj = new BeanDefined();
        beanObj.setBeanId("teacher");
        beanObj.setClassPath("com.kaikeba.beans.Teacher");
        /*
         *  <property>
         *
         **/
        Map<String, String> propertyMap = beanObj.getPropertyMap();
        propertyMap.put("teacherName", "李老师");
        propertyMap.put("friendArray", "老刘,老孙,小师妹");
        propertyMap.put("school", "清华一小,北京理工大学");


        List configuration = new ArrayList();
        configuration.add(beanObj);//spring核心配置


        //2.声明一个Spring提供BeanFacotory
        BeanFactory factory = new BeanFactory(configuration);


        //3.开发人员向BeanFactory索要实例对象.
        Teacher t = (Teacher) factory.getBean("teacher");
        System.out.println("t=" + t);
        System.out.println(t.getTeacherName());


    }

}
