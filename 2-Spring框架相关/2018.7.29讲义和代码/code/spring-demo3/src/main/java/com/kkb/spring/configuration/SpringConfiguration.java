package com.kkb.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import com.kkb.spring.service.UserService;
import com.kkb.spring.service.UserServiceImpl;

//@Configuration,相当于一个spring的xml配置文件中的beans根标签
//<beans></beans>
//该类一般被称之为配置类
@Configuration
@ComponentScan(basePackages = "com.kkb.spring.service")
// 相当于context:component-scan标签
@Import(DaoConfiguration.class)
public class SpringConfiguration {

	public SpringConfiguration() {
		System.out.println("容器初始化...");
	}

	// 纯注解方式之IoC配置，方式一：通过@Bean注解
	// Bean注解，可以指定bean的id，如果不知道，默认bean的id就是@Bean注解对应的方法名称
	// 相当于原先spring配置文件中的bean标签
	// @Bean(value="userService")
	// @Bean
	// @Scope("prototype")
	// @Scope("singleton")
	// public UserService userService() {
	// return new UserServiceImpl();
	// }

}
