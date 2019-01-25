package com.hptg.application;

import com.hptg.mapper.DeptMapper;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

public class TestMain{
    private SqlSession session;
    @Before
    public void start(){
        try{
            InputStream inputStream = Resources.getResourceAsStream("myBatis-config.xml");
            InputStream inputStream2= Resources.getResourceAsStream("config.properties");
            Properties properties = new Properties();
            properties.load(inputStream2);
            //SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(inputStream, null, properties);
            SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(inputStream, "development");
            session=factory.openSession();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    @Test
    public void test(){
        DeptMapper dao = session.getMapper(DeptMapper.class);
        Dept dept = new Dept();
        dept.setDname("Name");
        dept.setLoc("TX");
        dao.deptSave(dept);
        session.commit();
    }



    @Test
    public void test01(){
        Dept dept = new Dept();
        dept.setDname("风控部2");
        dept.setLoc("北京");

        try{
            session.insert("Dept.saveDept", dept);
            session.commit();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void test02(){


        try{
            Dept dept= session.selectOne("deptFindById", 1019);
            System.out.println(dept.getDname());
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test03(){
        try{
            DeptMapper dao=	session.getMapper(DeptMapper.class);
            Dept dept=dao.deptFindById(1019);
            System.out.println(dept.getDname());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @After
    public void end(){
        if(session!=null){
            session.close();
        }
    }

}

