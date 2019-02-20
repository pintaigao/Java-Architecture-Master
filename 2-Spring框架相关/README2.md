# Spring框架相关知识

### 一. 基础知识点梳理

Spring 框架的核心就是IOC和AOP

> IOC 简单理解就是控制对象创建角色由程序员反转为Spring IOC容器
>
> AOP 简单理解就是针对目标对象进行动态代理，横向增强JavaBean的功能
>
> **动态代理**：有两种技术，一个是基于JDK（实现接口），另一个是使用Calib

一. Spring IOC容器

* Spring IOC 容器本质上就是创建**类的实例**的工厂，并且对**类的实例**进行管理，

* Spring IOC 容器需要通过Bean工厂来实现，在Spring框架中，主要有两个工厂接口：**BeanFactory** 接口和**ApplicationContext** 接口（实现了BeanFactory接口）

  > 其中BeanFactory 接口是Spring早期创建Bean对象的工厂接口，而我们现在大多数是通过ApplicationContext 接口进行Bean工厂的创建

* Spring IOC 容器加载Bean信息的方式有XML配置方式和注解方式

  > XML 配置方式：<bean />
  >
  > 注解方式：@Component，@Controller, @Service, @Repository，需要使用<context:component-scan /> 配合使用

* Spring IOC 容器的创建方式主要有两种场景：在Java Application中创建**jar包**和在Web Application **war包**中创建（重点）。

  >​    在 Java Application 中创建 Spring IoC 容器主要是通过 **ApplicationContext** 接口的两个实现类来完成的：**ClassPathXmlApplicationContext** 和**FileSystemXmlApplicationContext**。
  >
  >​    在 Web Application 中创建Spring IoC容器主要是通过ApplicationContext接口的子接口WebApplicationContext来实现的。WebApplicationContext是通过ContextLoaderListener（实现ServletContextListener接口）创建之后，放入ServletContext域对象中的。

* Spring DI（依赖注入）是基于IoC使用的。简单理解就是Bean工厂在生成Bean对象的时候，如果Bean对象需要装配一个属性，那么就会通过DI将属性值注入给对象的属性。

  > 依赖注入的方式**主要有**构造方法注入（了解）**和**set方法注入（重点）。
  >
  > set方法注入**又分为**手动装配方式注入**和**自动装配方式注入。
  >
  > 手动装配方式（XML方式）：bean标签的子标签property，需要在类中指定set方法。
  >
  > 自动装配方式（注解方式）：**@Autowired注解、@Resource注解**。缺点：如果多个类存在相同名字的情况下，这个Autowire就会出错
  >
  > @Autowired：一部分功能是**查找实例**，从spring容器中根据类型（java类）获取对应的实例。另一部分功能就是**赋值**，将找到的实例，装配给另一个实例的属性值。（注意事项：一个java类型在同一个spring容器中，只能有一个实例）
  >
  > @Resource：一部分功能是**查找实例**，从spring容器中根据Bean的名称（bean标签的名称）获取对应的实例。另一部分功能就是**赋值**，将找到的实例，装配给另一个实例的属性值。

二. Spring AOP

* Spring AOP 实现原理是什么？

  > 动态代理技术（反射）：基于JDK的动态代理和使用CGLib的动态代理。
  >
  > 动态代理方式选择：根据是否实现接口来选择哪种代理方式。



### 二. Spring 容器初始化过程