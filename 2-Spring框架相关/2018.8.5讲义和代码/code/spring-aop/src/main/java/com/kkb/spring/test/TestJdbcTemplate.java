package com.kkb.spring.test;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TestJdbcTemplate {

	@Test
	public void test() {
		 // 创建连接池，先使用Spring框架内置的连接池
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql:///kkb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        // 创建模板类
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);//我们自己编写原始的JDBC代码，可能需要9步
        // 完成数据的添加
        jdbcTemplate.update("insert into account values (null,?,?)", "测试",10000);

	}
}
