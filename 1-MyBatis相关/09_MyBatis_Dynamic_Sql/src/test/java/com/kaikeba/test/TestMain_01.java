package com.kaikeba.test;


import java.io.InputStream;
import java.util.List;

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
    	 DeptMapper dao=  session.getMapper(DeptMapper.class);
    	 Dept dept = new Dept();
    	 dept.setDeptNo(20);
    	 dept.setDname("SE");
    	 List list=dao.dept_1(dept);
    	 System.out.println(list.size());
    }
    
    @Test
    public void test02(){
    	try{
    		EmpMapper dao=  session.getMapper(EmpMapper.class);
       	    Employee employee = new Employee();
       	    employee.setSal(1000.0);
       	    List list=dao.empFind(employee);
       	    System.out.println(list.size());
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
   }
    
    @Test
    public void test03(){
    	 DeptMapper dao=  session.getMapper(DeptMapper.class);
    	 Dept dept = new Dept();  
    	 dept.setDname("ES");
    	 List list=dao.dept_2(dept);
    	 System.out.println(list.size());
    }
    
    @Test
    public void test04(){
    	 DeptMapper dao=  session.getMapper(DeptMapper.class);
    	 Dept dept = new Dept();  
    	 dept.setDeptNo(20);
    	 dept.setDname("新部门");
    	 dao.deptUpdate(dept);
    	
    }
    
    @Test
    public void test05(){
    	 DeptMapper dao=  session.getMapper(DeptMapper.class);
    	 Dept dept = new Dept();  
    	 dept.setDname("ACCOUNTING");
    	 dept.setLoc("BeiJing");
    	 List list=dao.dept_3(dept);
    	 System.out.println(list.size());
    }
    
   
    @After
    public void end(){
    	if(session!=null){
    		session.close();
    	}
    }
    
}
