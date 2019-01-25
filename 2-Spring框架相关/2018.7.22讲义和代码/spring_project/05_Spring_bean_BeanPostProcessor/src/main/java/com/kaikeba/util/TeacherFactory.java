package com.kaikeba.util;

import org.springframework.beans.factory.config.BeanPostProcessor;

import com.kaikeba.beans.Teacher;

public class TeacherFactory {
      //节省内存消耗
	  public static Teacher createTeacher(){
		      Teacher teacher =    new Teacher();
		      System.out.println("TeacherFactory 负责创建 teacher类实例对象..");
		      return teacher;
	  }
	  BeanPostProcessor cc;
}
