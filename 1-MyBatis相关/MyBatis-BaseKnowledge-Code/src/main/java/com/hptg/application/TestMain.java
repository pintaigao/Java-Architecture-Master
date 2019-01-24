package com.hptg.application;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class TestMain {

	public static void main(String[] args) throws Exception {
		Dept dept = new Dept();
		dept.setDname("国防部");
		dept.setLoc("美国");
		InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
		SqlSession session = factory.openSession();
		session.insert("insertDept",dept);
		session.commit();
		session.close();
	}

}
