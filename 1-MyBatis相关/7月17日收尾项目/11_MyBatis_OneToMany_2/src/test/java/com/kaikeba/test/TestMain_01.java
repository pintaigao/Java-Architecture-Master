package com.kaikeba.test;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kaikeba.beans.Dept;
import com.kaikeba.beans.Employee;
import com.kaikeba.dao.DeptMapper;
import com.kaikeba.dao.EmpMapper;

public class TestMain_01 {
    private SqlSession session;
    @Before
	public void start(){
		try{
			InputStream inputStream = Resources.getResourceAsStream("myBatis-config.xml");
			SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(inputStream);
			session=factory.openSession();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
    @Test
    public void test01(){
    	try{
    		 DeptMapper dao=  session.getMapper(DeptMapper.class);
        	 Dept dept=dao.deptFindByDeptNo(10);
        	 System.out.println();
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
   
    @After
    public void end(){
    	if(session!=null){
    		session.close();
    	}
    }
    
}
