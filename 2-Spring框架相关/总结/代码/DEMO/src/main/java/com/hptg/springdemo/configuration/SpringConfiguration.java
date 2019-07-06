package com.hptg.springdemo.configuration;

import com.hptg.springdemo.service.UserService;
import com.hptg.springdemo.service.UserServiceImpl3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * 案例3：基于纯注解的Spring应用案例(applicationContext.xml在这里是没有的)
 * 这一个Class，就相当于applicationContext.xml，最终在main中相当于ClassPathXmlApplicationContext("applicationContext.xml")的使用
 */
@Configuration
// 以下这个相当于context:component-scan标签
// @ComponentScan(basePackages = "com.hptg.springdemo.service") ==> 这个写完后就能扫描到basePackages中各种class的@Controller
@Import(DaoConfiguration.class) // ===>导入其他的配置文件，到时候这个SpringConfiguration就相当于主配置文件
@ComponentScan(basePackages = "com.hptg.springdemo.service")
public class SpringConfiguration {
    public SpringConfiguration() {
        System.out.println("容器初始化...");
    }

    /**
     * 纯注解方式之IoC配置，方式一：通过@Bean注解
     * Bean注解，可以指定bean的id，如果不知道，默认bean的id就是@Bean注解对应的方法名称
     * 相当于原先spring配置文件中的bean标签
     * 也相当于在UserServiceImpl3的class顶头写@Controller("UserServiceImpl3")
     */
    @Bean(value = "userService3")
    @Scope("prototype")
    /*@Bean
    @Scope("singleton")*/
    public UserService userService() {
        return new UserServiceImpl3();
    }

}