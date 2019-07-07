package com.hptg.springdemo.test;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


/**
 * 一. 这个是最基础的JDBCTemplate， 没有用到任何Spring的东西
 * */
public class TestJdbcTemplate {

	@Test
	public void test() {
		// 创建连接池，先使用Spring框架内置的连接池
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://192.168.2.7:3306/Tutor");
		dataSource.setUsername("hptg");
		dataSource.setPassword("Hptg19940215");
		// 创建模板类
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);//我们自己编写原始的JDBC代码，可能需要9步
		// 完成数据的添加
		jdbcTemplate.update("insert into tutor_table values (NULL, ?)", "Pintaigao HE");

	}
}
