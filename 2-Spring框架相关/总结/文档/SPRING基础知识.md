# Spring介绍

## 一. 什么是Spring

这个就自己上网查吧



## 二. Spring 核心概念介绍

l  **IoC（核心中的核心）：Inverse of Control，控制反转。**对象的创建权力由程序反转给Spring框架。

l  **AOP：Aspect Oriented Programming，面向切面编程。**在不修改目标对象的源代码情况下，增强IoC容器中Bean的功能。

l  **DI：Dependency Injection，依赖注入。**在Spring框架负责创建Bean对象时，动态的将依赖对象注入到Bean组件中！！

l  **Spring容器：指的就是IoC容器。**



## 三.Spring IOC 原理分析

导读：要想使用Spring IoC，必须要创建Spring IoC容器。

* **那么什么是IoC容器呢？**

* **如何创建IoC容器呢？**

* **IoC容器是如何初始化Bean实例的呢？**

**3.1 什么是IOC容器**

* 所谓的IoC容器就是指的Spring中Bean工厂里面的Map存储结构（存储了Bean的实例）。

**3.2 Spring框架中的工厂有哪些**

* **ApplicationContext**接口
  * 实现了BeanFactory接口
  * 实现ApplicationContext接口的工厂，可以获取到容器中具体的Bean对象
* **BeanFactory**工厂（是Spring框架早期的创建Bean对象的工厂接口）
  * 实现BeanFactory接口的工厂也可以获取到Bean对象

**其实通过源码分析，不管是BeanFactory还是ApplicationContext，其实最终的底层BeanFactory都是DefaultListableBeanFactory**

* ApplicationContext和BeanFactory的区别？
  * 创建Bean对象的时机不同:
    * **BeanFactory采取**延迟加载，第一次getBean时才会初始化Bean。
    *  ApplicationContext是加载完applicationContext.xml时，就创建具体的Bean对象的实例。（只对BeanDefition中描述为是单例的bean，才进行饿汉式加载）
* <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC2.png" width=60% align=left />
* <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC1.png" width=60% align=left />

**3.3 如何创建Web环境中的Ioc容器**

* ApplicationContext接口常用实现类

  * ClassPathXmlApplicationContext:它是从类的根路径下加载配置文件   推荐使用这种
  * FileSystemXmlApplicationContext:它是从磁盘路径上加载配置文件，配置文件可以在磁盘的任意位置。
  * AnnotationConfigApplicationContext:当我们使用注解配置容器对象时，需要使用此类来创建 spring 容器。它用来读取注解。

* Java应用中创建IoC容器：（了解）

  * ApplicationContext context = new ClassPathXmlApplicationContext(xml路径);

* **Web应用中创建IoC容器：（重点）**

  * web.xml中配置ContextLoaderListener接口，并配置ContextConfigLocation参数
  * web容器启动之后加载web.xml，此时加载**ContextLoaderListener**监听器（*实现了ServletContextListener接口，该接口的描述请见下面《三类八种监听器》*）
  * ContextLoaderListener监听器会在web容器启动的时候，触发**contextInitialized**()方法。
  * contextInitialized()方法会调用**initWebApplicationContext**()方法，该方法负责创建**Spring容器（DefaultListableBeanFactory）**。

* 源码分析

  1. web服务器（tomcat）启动会加载web.xml（启动**ContextLoaderListener**监听器）:

     <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC3.png" width=60% align=left />

  2. 调用ContextLoaderListener类的contextInitializd方法，创建Web环境中的Spring上下文对象。

     <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC4.png" width=60% align=left />

  3. 调用ContextLoader类的initWebApplicationContext方法

     <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC5.png" width=60% align=left />

  4. 继续调用ContextLoader类的configureAndRefreshWebApplicationContext方法，该方法中调用最终初始化Bean的**refresh**方法

     <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC6.png" width=70% align=left />

  **总结起来就是**

  <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC7.png" width=40% align=left /
  
  
  
* 容器初始化的源码

  1. 主流程入口

     ```java
     ApplicationContext context = new ClassPathXmlApplicationContext（“spring.xml”）
     ```

  2. **ClassPathXmlApplicationContext**类：重载的构造方法依次调用，进入下面代码

     <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC8.png" width=70% align=left />

  3. **AbstractApplicationContext的refresh方法：初始化spring容器的核心代码**

     ```java
     @Override
     public void refresh() throws BeansException, IllegalStateException {
     	synchronized (this.startupShutdownMonitor) {
     		//1、 Prepare this context for refreshing.
         prepareRefresh();
     		//创建DefaultListableBeanFactory（真正生产和管理bean的容器）
         //加载BeanDefition并注册到BeanDefitionRegistry
         //通过NamespaceHandler解析自定义标签的功能（比如:context标签、aop标签、tx标签）
     		//2、 Tell the subclass to refresh the internal bean factory.
     		ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
     		//3、 Prepare the bean factory for use in this context.
         prepareBeanFactory(beanFactory);
     		try {
           //4、 Allows post-processing of the bean factory in context subclasses.
     			postProcessBeanFactory(beanFactory);
           //实例化并调用实现了BeanFactoryPostProcessor接口的Bean
     	    //比如：PropertyPlaceHolderConfigurer（context:property-placeholer）
       		//就是此处被调用的，作用是替换掉BeanDefinition中的占位符（${}）中的内容
     			//5、 Invoke factory processors registered as beans in the context.
     			invokeBeanFactoryPostProcessors(beanFactory);
           //创建并注册BeanPostProcessor到BeanFactory中（Bean的后置处理器）
     			//比如：AutowiredAnnotationBeanPostProcessor（实现@Autowired注解功能）
         	//RequiredAnnotationBeanPostProcessor（实现@d注解功能）
           //这些注册的BeanPostProcessor
     			//6、 Register bean processors that intercept bean creation.
     			registerBeanPostProcessors(beanFactory);
           //7、 Initialize message source for this context.
     			initMessageSource();
     			//8、 Initialize event multicaster for this context.
     			initApplicationEventMulticaster();
     			//9、 Initialize other special beans in specific context subclasses.
     			onRefresh();
     			//10、 Check for listener beans and register them.
     			registerListeners();
     			//创建非懒加载方式的单例Bean实例（未设置属性）
           //填充属性
           //初始化实例（比如调用init-method方法）
           //调用BeanPostProcessor（后置处理器）对实例bean进行后置处理
     			//11、 Instantiate all remaining (non-lazy-init) singletons.
     			finishBeanFactoryInitialization(beanFactory);
     			//12、 Last step: publish corresponding event.
     			finishRefresh();
     		}
     		catch (BeansException ex) {
           // Destroy already created singletons to avoid dangling resources.
     			destroyBeans();
     			// Reset 'active' flag.
     			cancelRefresh(ex);
     			// Propagate exception to caller.
     			throw ex;
         }finally {
           // Reset common introspection caches in Spring's core, since we
     			// might not ever need metadata for singleton beans anymore...
     			resetCommonCaches();
         }
       }
     }  
     ```
     
  4. 总结起来：
  
     <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC9.png" width=55% align=left />
  
* 创建BeanFactory流程分析

  * 获取新的BeanFactory子流程

    1. 子流程入口（从主流程refresh方法中的第二步开始）

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC10.png" width=55% align=left />

    2. 调用AbstractApplicationContext中的obtainFreshBeanFactory方法

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC11.png" width=55% align=left />

    3. 调用AbstractRefreshableApplicationContext的refreshBeanFactory方法

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC12.png" width=55% align=left />

  * 加载解析BeanDefinition子流程（loadDefinitions方法）

    1. 子流程入口（AbstractRefreshableApplicationContext类的方法）

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC13.png" width=55% align=left />

       此处依次调用多个类的loadBeanDefinitions方法（AbstractXmlApplicationContextà
       AbstractBeanDefinitionReaderà XmlBeanDefinitionReader），一直调用到XmlBeanDefinitionReader 类的doLoadBeanDefinitions方法

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC14.png" width=55% align=left />

       对于doLoadDocument方法不是我们关注的重点，我们进入到该类的registerBeanDefinitions方法看看

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC15.png" width=90% align=left />

       此处有两个地方是我们关注的：一个createRederContext方法，一个是DefaultBeanDefinitionDocumentReader类的registerBeanDefinitions方法，先进入createRederContext方法看看

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC16.png" width=90% align=left />

       至此，14个NamespaceHandlerResolver初始化成功。然后我们再进入DefaultBeanDefinitionDocumentReader类的registerBeanDefinitions方法

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC17.png" width=70% align=left />

       继续进入到该类的doRegisterBeanDefinitions方法看看，这是真正干活的方法

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC18.png" width=80% align=left />

       继续进入parseBeanDefinitions方法

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC19.png" width=70% align=left />

       我们看到有两种解析方案，先看看parseDefaultElement方法

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC20.png" width=70% align=left />

       不过我们重点看看BeanDefinitionParserDelegate类的parseCustomElement方法（AOP标签、tx标签的解析都是在该步骤中完成的）

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC21.png" width=70% align=left />

       getNamespaceURI方法的作用一目了然，我们就不去追踪了，接下来我们进入DefaultNamespaceHandlerResolver类的resolve方法看看：

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC22.png" width=70% align=left />

       在上面代码中，我们看到了一行代码：namespaceHandler.init();这个方法是很重要的。它实现了自定义标签到处理类的注册工作，不过NamespaceHandler是一个接口，具体的init方法需要不同的实现类进行实现，我们通过**AopNamespaceHandler**了解一下init的作用，其中**aop:config标签是由ConfigBeanDefinitionParser类进行处理**：

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC23.png" width=70% align=left />

       至此，我们了解到了xml中的aop标签都是由哪些类进行处理的了。不过init方法只是注册了标签和处理类的对应关系，那么什么时候调用处理类进行解析的呢？我们再回到BeanDefinitionParserDelegate类的parseCustomElement方法看看

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC24.png" width=70% align=left />

       我们看到，最后一行执行了parse方法，那么parse方法，在哪呢？我们需要到NamespaceHandlerSupport类中去看看，它是实现NamespaceHandler接口的，并且AopNamespaceHandler是继承了NamespaceHandlerSupport类，那么该方法也会继承到AopNamespaceHandler类中。

       <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC25.png" width=70% align=left />

       至此，整个XML文档的解析工作，包括bean标签以及自定义标签如何解析为BeanDefinition信息的过程，我们已经了解了。

       **后续具体想了解哪个自定义标签的处理逻辑，可以自行去查找xxxNamespaceHandler类进行分析。**

* 创建Bean流程分析

  * 子流程入口

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC26.png" width=50% align=left />

  * 我们进入finishBeanFactoryInitialization方法看看：

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC27.png" align=left />

  * 继续进入DefaultListableBeanFactory类的preInstantiateSingletons方法，我们找到下面部分的代码，看到工厂Bean或者普通Bean，最终都是通过getBean的方法获取实例的。

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC28.png" width = 70% align=left />

  * 继续跟踪下去，我们进入到了AbstractBeanFactory类的doGetBean方法，这个方法中的代码很多，我们直接找到核心部分：

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC29.png" width = 60% align=left />

  * 接着进入到AbstractAutowireCapableBeanFactory类的方法，找到以下代码部分

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC30.png" width = 90% align=left />

  * 我们终于找到核心的地方了，进入doCreateBean方法看看，该方法我们关注两块重点区域：

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC31.png" width = 80% align=left />

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC32.png" width = 50% align=left />

  * 对于如何创建Bean的实例，和填充属性，暂时先不去追踪了，我们先去看看initializeBean方法是如何调用BeanPostProcessor的，因为这个牵扯到我们对于AOP动态代理的理解。

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC33.png" width = 90% align=left />

  * 至此，如何创建Bean，以及AOP在哪产生代理的步骤，我们已经分析过了。
  
    

## 四.Spring IoC基于XML的使用
#### 1.具体例子

1. XML准备

   ```xml
   略
   ```

2. 具体实现（接口+实现类+XML）

   接口：

   ```java
   public interface UserService { void saveUser();}
   ```

   实现类：

   ```java
   public class UserServiceImpl implements UserService {
     @Override
     public void saveUser() { 
       // do something 
     }
   }
   ```

   XML:

   <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC34.png" width = 50% align=left />

   测试：

   <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC35.png" width = 50% align=left />

#### 2.Bean 标签详解

略



## 五.Spring DI(依赖注入)介绍

#### 1.概述

**什么是依赖？**

* 依赖指的就是Bean实例中的**属性**
* 属性分为：简单类型（8种基本类型和String类型）的属性、POJO类型的属性、集合数组类型的属性。

**什么是依赖注入**

* 依赖注入：Dependency Injection。它是 spring 框架核心 ioc 的具体实现。

**为什么要进行依赖注入？**

* 我们的程序在编写时，通过控制反转，把对象的创建交给了 spring，但是代码中不可能出现没有依赖的情况。
* ioc 解耦只是降低他们的依赖关系，但不会消除。例如：我们的业务层仍会调用持久层的方法。那这种业务层和持久层的依赖关系，在使用 spring 之后，就让 spring 来维护了。
* 简单的说，就是坐等框架把持久层对象传入业务层，而不用我们自己去获取。

#### 2. 依赖注入的方式

1. **构造函数的注入**

   顾名思义，就是使用类中的构造函数，给成员变量赋值。

   ```java
   public class UserServiceImpl implements UserService {
   	private int id;
   	private String name;
   	
   	public UserServiceImpl(int id, String name) {
   		this.id = id;
   		this.name = name;
   	}
   	@Override
   	public void saveUser() {
   		System.out.println("保存用户:id为"+id+"，name为"+name+"   Service实现");
   	}
   }
   ```

2. **set方法注入（重点）**

   set方法注入**又分为**手动装配方式注入**和**自动装配方式注入。

   * 手动装配方式（XML方式）：bean标签的子标签property，需要在类中指定set方法。

   * 自动装配方式（注解方式，后面讲解）：@Autowired注解、@Resource注解。
     * @Autowired：一部分功能是**查找实例**，从spring容器中根据类型（java类）获取对应的实例。另一部分功能就是**赋值**，将找到的实例，装配给另一个实例的属性值。（注意事项：一个java类型在同一个spring容器中，只能有一个实例）
     * @Resource：一部分功能是**查找实例**，从spring容器中根据Bean的名称（bean标签的名称）获取对应的实例。另一部分功能就是**赋值**，将找到的实例，装配给另一个实例的属性值。

#### 3.依赖注入不同类型的属性

* 简单类型

  ```xml
  <bean id="userService" class="com.kkb.spring.service.UserServiceImpl">
  	<property name="id" value="1"></property>
  	<property name="name" value="zhangsan"></property>
  </bean>
  ```

* 引用类型

  ```xml
  ref就是reference的缩写，是引用的意思。
  <bean id="userService" class="com.kkb.spring.service.UserServiceImpl">
  		<property name="userDao" ref="userDao"></constructor-arg>
  </bean>
  <bean id="userDao" class="com.kkb.spring.dao.UserDaoImpl"></bean>
  ```

* 集合类型

  ```xml
  1. 如果是数组或者List集合，注入配置文件的方式是一样的
      <bean id="collectionBean" class="com.kkb.demo5.CollectionBean">
        <property name="arrs">
          <list>
  				//如果集合内是简单类型，使用value子标签，如果是POJO类型，则使用bean标签
          	<value>美美</value>
            <value>小风</value>
  					<bean></bean>
          </list>
        </property>
      </bean>
  
  2. 如果是Set集合，注入的配置文件方式如下：
      <property name="sets">
        <set>
  			//如果集合内是简单类型，使用value子标签，如果是POJO类型，则使用bean标签
        	<value>哈哈</value>
  	      <value>呵呵</value>
        </set>
      </property>
  
  3. 如果是Map集合，注入的配置方式如下：
      <property name="map">
        <map>
        	<entry key="老王2" value="38"/>
          <entry key="凤姐" value="38"/>
          <entry key="如花" value="29"/>
        </map>
      </property>
  
  4. 如果是Properties集合的方式，注入的配置如下：
      <property name="pro">
        <props>
        	<prop key="uname">root</prop>
          <prop key="pass">123</prop>
        </props>
      </property>
  ```



## 6 Spring IOC 和 DI 基于注解(Annotation)使用

#### 1. IOC注解使用

* 第一步：Spring配置文件中，配置context:component-scan标签

  <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC36.png" width = 80% align=left />

* 第二步：类上面加上注解@Component，或者它的衍生注解**@Controller、@Service、@Repository**

  <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC37.png" width = 50% align=left />

#### 2.常用注解

1. IOC 注解（创建对象，相当于：<bean id="" class="">）

   * @Component注解
   * @Controller、@Service、@Repository注解

2. DI注解（依赖注入）

  以下相当于：<property name="" ref="">
  
  * @Autowired
  * @Qualifier
  * @Resource
  
  以下相当于：**<property name="" value="">**
  
  * @Value
  
3. 改变作用范围

   * @Scope（相当于<bean id="" class="" scope="">）
   
4. 和生命周期相关

   * @PostConstruct和@PreDestroy（相当于：<bean id="" class="" init-method="" destroy-method="" />）



## 7.Spring的纯注解配置

前言：写到此处，基于注解的 IoC 配置已经完成，但是大家都发现了一个问题：我们依然离不开 spring 的 xml 配置文件，那么能不能**不写**这个 applicationContext.xml，所有配置都用注解来实现呢？

之前的：

* 注解扫描配置（能不能去掉）

  ```xml
  <!-- 开启注解并扫描指定包中带有注解的类 -->
  <context:component-scan base-package="com.kkb.spring.service"/>
  ```

* 非自定义的Bean配置（比如：SqlSessionFactory和BasicDataSource配置）

  ```xml
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  	<property name="dataSource" value="dataSource"></property>
  </bean>
  ```

* 去掉XML后，如何创建ApplicationContext

  ```Java
  //之前创建ApplicationContext都是通过读取XML文件进行创建的。
  ApplicationContext context = new ClassPathXmlApplicationContext(“beans.xml”);
  ```

#### 1. 新的注解

* @Configuration（相当于<beans>根标签）

  * **介绍：**

    从Spring3.0，@Configuration用于定义**配置类**，可替换xml配置文件

    相当于<beans>根标签

    配置类**内部包含有一个或多个被**@Bean注解**的方法，这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，并用于构建bean定义，初始化Spring容器。
    
  * **属性：**

    value:用于指定配置类的字节码

  * **示例代码**：
  
    ```java
    @Configuration
    public class SpringConfiguration {
    	//spring容器初始化时，会调用配置类的无参构造函数
      public SpringConfiguration(){
    		System.out.println(“容器启动初始化。。。”);
    	}
    }
    ```
  
* @Bean

  * 介绍

    * @Bean标注在方法上(返回某个实例的方法)，等价于spring配置文件中的<bean>
    * 作用为：注册bean对象
    * 主要用来配置非自定义的bean，比如DruidDataSource、SqlSessionFactory

  * 属性：

    * name：给当前@Bean 注解方法创建的对象指定一个名称(即 bean 的 id）
    * 如果不指定，默认与标注的方法名相同
    * @Bean注解默认作用域为单例singleton作用域，可通过@Scope(“prototype”)设置为原型作用域；

  * **示例代码：**

    ```java
    @Configuration
    public class SpringConfiguration {
    	//spring容器初始化时，会调用配置类的无参构造函数
      public SpringConfiguration(){
    		System.out.println(“容器启动初始化。。。”);
    	}
    	@Bean
    	@Scope(“prototype”)
    	public UserService userService(){
    		return new UserServiceImpl(1,“张三”);
    	}
    }
    ```

* @ComponentScan

  * 介绍

    * 相当于context:component-scan标签
    * 组件扫描器，扫描@Component、@Controller、@Service、@Repository注解的类。
    * 该注解是编写在类上面的，一般配合@Configuration注解一起使用。

  * 属性：

    * basePackages：用于指定要扫描的包。
    * value：和basePackages作用一样。

  * 示例代码：

    * Bean类（Service类）：

      ```java
      @Service
      public class UserServiceImpl implements UserService {
      	@Override
      	public void saveUser() {
      		System.out.println("保存用户   Service实现");
      	}
      }
      ```

    * 配置类：

      ```java
      @Configuration
      @ComponentScan(basePackages="com.kkb.spring.service")
      public class SpringConfiguration {
      
      	public SpringConfiguration() {
      		System.out.println("容器初始化...");
      	}
      	
      //	@Bean
      //	@Scope("prototype")
      //	public UserService userService() {
      //		return new UserServiceImpl(1,"张三");
      //	}
      }
      ```
      
      

* @PropertySource

  * 介绍
    * 加载properties配置文件
    * 编写在类上面
    * 相当于context:property-placeholder标签
  
  * 属性

    * value[]：用于指定properties文件路径，如果在类路径下，需要写上classpath
  
  * 示例代码
  
    * 配置类：
  
      ```java
      @Configuration
      @PropertySource(“classpath:jdbc.properties”)
      public class JdbcConfig {
      	@Value("${jdbc.driver}")
      	private String driver;
      	@Value("${jdbc.url}")
      	private String url;
      	@Value("${jdbc.username}")
      	private String username;
      	@Value("${jdbc.password}")
      	private String password;
      
       /**
      	 *	创建一个数据源，并存入 spring 容器中
      	 *	@return
      	**/
      	@Bean(name="dataSource")
      	public DataSource createDataSource() {
      		try {
      			ComboPooledDataSource ds = new ComboPooledDataSource(); 			
            ds.setDriverClass(driver);
            ds.setJdbcUrl(url); 
            ds.setUser(username); 
            ds.setPassword(password); return ds;
      		} catch (Exception e) {
      			throw new RuntimeException(e);
      		}
      	}
      }
      ```
  
    * properties文件：
  
      ```properties
      jdbc.driver=com.mysql.jdbc.Driver 
      jdbc.url=jdbc:mysql:///spring
      jdbc.username=root 
      jdbc.password=root
      ```
  
    * 问题
  
      当系统中有多个配置类时怎么办呢？想一想之前使用XML配置的时候是如何解决该问题的。
  
* @Import

  * 介绍

    * 用来组合多个配置类
    * 相当于spring配置文件中的import标签
    * 在引入其他配置类时，可以不用再写@Configuration 注解。当然，写上也没问题。

  * 属性

    * value：用来指定其他配置类的字节码文件

  * 示例代码

    ```java
    @Configuration
    @ComponentScan(basePackages = "com.kkb.spring")
    @Import({ JdbcConfig.class})
    public class SpringConfiguration {
    }
    
    @Configuration
    @PropertySource("classpath:jdbc.properties")
    public class JdbcConfig{
    }
    ```

  

* 通过注解获取容器

  * Java应用

    ```java
    ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    UserService service = context.getBean(UserService.class);
    service.saveUser();
    ```

  * Web应用（AnnotationConfigWebApplicationContext，后面讲解）

    ```xml
    <web-app>
        <context-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.springframework.web.context.
                support.AnnotationConfigWebApplicationContext
            </param-value>
        </context-param>
        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>
                com.kkb.spring.test.SpringConfiguration
            </param-value>
        </context-param>
        <listener>
            <listener-class>
                org.springframework.web.context.ContextLoaderListener
            </listener-class>
        </listener>
    </web-app>
    ```



# 8.Spring 分模块开发

略



## 9.IOC和DI总结

略



## 10.Spring 整合 Junit

略



## 11.Spring AOP 原理分析

#### 一. 简介

* 在软件业，AOP为Aspect Oriented Programming的缩写，意为：面向**切面**编程
* AOP是一种编程范式，隶属于软工范畴，指导开发者如何组织程序结构
* AOP最早由**AOP联盟的组织提出**的,制定了一套规范.Spring将AOP思想引入到框架中,必须遵守AOP联盟的规范
* 通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术
* AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型
* 利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率

#### 二.为什么使用AOP

1. 作用
   * AOP采取横向抽取机制，补充了传统纵向继承体系（OOP）无法解决的重复性代码优化（性能监视、事务管理、安全检查、缓存）
   * **将业务逻辑和系统处理的代码（关闭连接、事务管理、操作日志记录）解耦。**
   
2. 优势

   * 重复性代码被抽取出来之后，维护更加方便

3. 纵向继承体系

   <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC38.png" width = 70% align=left />

4. 横向抽取机制：

   <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/IOC39.png" width = 60% align=left />

   

#### 三.AOP相关术语介绍

1. 术语解释

   * **Joinpoint(连接点)**   -- 所谓连接点是指那些被拦截到的点。在spring中,这些点指的是方法,因为spring只支持方法类型的连接点

   * **Pointcut(切入点)**        -- 所谓切入点是指我们要对哪些Joinpoint进行拦截的定义

   * **Advice(通知/增强)**    -- 所谓通知是指拦截到Joinpoint之后所要做的事情就是通知.通知分为前置通知,后置通知,异常通知,最终通知,环绕通知(切面要完成的功能)

   * **Introduction(引介)** -- 引介是一种特殊的通知在不修改类代码的前提下, Introduction可以在运行期为类动态地添加一些方法或Field

   * **Target(目标对象)**     -- 代理的目标对象

   * **Weaving(织入)**      -- 是指把增强应用到目标对象来创建新的代理对象的过程

   * **Proxy（代理）**       -- 一个类被AOP织入增强后，就产生一个结果代理类

   * **Aspect(切面)**        -- 是切入点和通知的结合，以后咱们自己来编写和配置的

   * **Advisor（通知器、顾问）**          --和Aspect很相似

     <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/AOP1.png" width = 90% align=left />

2. AOP实现之Spring AOP

   略



## 12.Spring基于AspectJ的AOP使用

其实就是指的Spring + AspectJ整合，不过Spring已经将AspectJ收录到自身的框架中了。

#### 一.开发

1. 编写通知（增强类，一个普通的类）

   ```java
   public class MyAdvice {
       //演示前置通知
       public void log() {
           System.out.println("开始记录日志了...");
       }
   }
   ```

2. **配置通知，将通知类交给spring IoC容器管理**

   ```xml
   <bean id="myAdvice" class="com.hptg.springdemo.advice.MyAdvice" />
   ```

3. **配置AOP 切面**

   <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/AOP2.png" width = 60% align=left />

4. 切入点表达式

   <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/AOP3.png" width = 70% align=left />

#### 二.通知类型

* 通知类型（五种）：前置通知、后置通知、最终通知、环绕通知、异常抛出通知。
* 前置通知：

  * 执行时机：目标对象方法之前执行通知

  * 配置文件：<aop:before method="before" pointcut-ref="myPointcut"/>

  * 应用场景：方法开始时可以进行校验
* 后置通知：
  * 执行时机：目标对象方法之后执行通知，**有异常则不执行了**
  * 配置文件：<aop:after-returning method="afterReturning" pointcut-ref="myPointcut"/>
  * 应用场景：可以修改方法的返回值

* 最终通知：
  * 执行时机：目标对象方法之后执行通知，**有没有异常都会执行**
  * 配置文件：<aop:after method="after" pointcut-ref="myPointcut"/>
  * 应用场景：例如像释放资源

* 环绕通知：
  * 执行时机：目标对象方法之前和之后都会执行。
  * 配置文件：<aop:around method="around" pointcut-ref="myPointcut"/>
  * 应用场景：事务、统计代码执行时机

* 异常抛出通知：

  * 执行时机：在抛出异常后通知

  * 配置文件：<aop:after-throwing method=" afterThrowing " pointcut- ref="myPointcut"/>

  * 应用场景：包装异常

    

#### 三.使用注解实现

* 编写切面类

  <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/AOP4.png" width = 50% align=left />

* **配置切面类**

  ```xml
  <context:component-scan base-package="com.kkb.spring"/>
  ```

* 开启AOP自动代理

  ```xml
  <aop:aspectj-autoproxy />
  ```

* 环绕通知注解配置

  **@Around**

  * 作用：把当前方法看成是环绕通知。

  * 属性：value：用于指定切入点表达式，还可以指定切入点表达式的引用。

    <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/AOP5.png" width = 50% align=left />

* 定义通用切入点

  **使用@PointCut注解在切面类中定义一个通用的切入点，其他通知可以引用该切入点**

  <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/AOP6.png" width = 50% align=left />

#### 四.纯注解的Spring AOP配置

```java
@Configuration
@ComponentScan(basePackages="com......")
@EnableAspectJAutoProxy
public class SpringConfiguration {
  // ...do something
}
```



## 13.Spring AOP 源码解析

<img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/AOP7.png" width = 80% align=left />



## 14.Spring应用之Spring JDBC实现

#### 一.Spring管理JdbcTemplate

1. Spring管理内置的连接池

   ```xml
   <bean id="dataSource"class="org.springframework.jdbc.datasource.DriverManagerDataSource">
   	<property name="driverClassName" value="com.mysql.jdbc.Driver"/>
     <property name="url" value="jdbc:mysql:///spring_day03"/>
     <property name="username" value="root"/>
     <property name="password" value="root"/>
   </bean>
   ```

2. Spring管理模版类

   ```xml
   <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
   	<property name="dataSource" ref="dataSource"/>
   </bean>
   ```

3. 编写测试程序

   ```java
   @RunWith(SpringJUnit4ClassRunner.class)
   @ContextConfiguration("classpath:applicationContext.xml")
   public class Demo2 {
     @Resource(name="jdbcTemplate")
     private JdbcTemplate jdbcTemplate;
   	@Test
     public void run(){
     	jdbcTemplate.update("insert into t_account values (null,?,?)", "测试2",10000);
      }
   }
   ```

#### 二.Spring管理第三方DataSource

1. 管理DBCP连接池

   * 先引入DBCP的2个jar包

   * 编写配置文件

     ```xml
     <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
     	<property name="driverClassName" value="com.mysql.jdbc.Driver"/>
       <property name="url" value="jdbc:mysql:///spring "/>
       <property name="username" value="root"/>
       <property name="password" value="root"/>
     </bean>
     ```

2. 管理C3P0连接池

   *   现引入C3P0的jar包

       ```xml
       com.springsource.com.mchange.v2.c3p0-0.9.1.2.jar
       ```

   *   编写配置文件

       ```xml
       <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
       	<property name="driverClass" value="com.mysql.jdbc.Driver"/>
         <property name="jdbcUrl" value="jdbc:mysql:///spring"/>
         <property name="user" value="root"/>
         <property name="password" value="root"/>
       </bean>
       ```

#### 三.使用JdbcTemplate完成增删改查操作

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDemo {
	@Resource(name="jdbcTemplate")
  private JdbcTemplate jdbcTemplate;
	@Test
  // 插入操作
  public void demo1(){
  	jdbcTemplate.update("insert into account values (null,?,?)", "冠希",10000d);
  }
	@Test
  // 修改操作
  public void demo2(){
  	jdbcTemplate.update("update account set name=?,money =? where id = ?", "思雨",10000d,5);
  }
	@Test
  // 删除操作
  public void demo3(){
  	jdbcTemplate.update("delete from account where id = ?", 5);
  }
  @Test
  // 查询一条记录
  public void demo4(){
  	Account account = jdbcTemplate.queryForObject("select * from account where id = ?", new BeanMapper(), 1);
    System.out.println(account);
  }
  @Test
  // 查询所有记录
  public void demo5(){
  	List<Account> list = jdbcTemplate.query("select * from t_account", new BeanMapper());
		for (Account account : list) {
    	System.out.println(account);
    }
  }
}
class BeanMapper implements RowMapper<Account>{
	public Account mapRow(ResultSet rs, int arg1) throws SQLException {
  	Account account = new Account();
	  account.setId(rs.getInt("id"));
  	account.setName(rs.getString("name"));
	  account.setMoney(rs.getDouble("money"));
	  return account;
	}
}
```



#### 四. Spring DAO 开发之JdbcDAOSupport

1.  案例设计

    *   编写转账案例（包括业务层和持久层）
    *   编写DAO时引入JdbcDaoSupport的使用

2.  实现

    *   步骤一：创建WEB工程，引入需要的jar包

        ```
        略
        ```

    *   步骤二：引入配置文件

        ```xml
        * 引入log4j.properties
        * 引入applicationContext.xml
        <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        	<property name="driverClass" value="com.mysql.jdbc.Driver"/>
          <property name="jdbcUrl" value="jdbc:mysql:///spring "/>
          <property name="user" value="root"/>
          <property name="password" value="root"/>
        </bean>
        ```

    *   步骤三：创建对应的包结构和类

        ```
        AccountService
        AccountServlceImpl
        AccountDao
        AccountDaoImpl
        ```

    *   步骤四：引入Spring的配置文件，将类配置到Spring中

        ```xml
        <bean id="accountService" class="com.kkb.demo1.AccountServiceImpl">
        </bean>
        <bean id="accountDao" class="com.kkb.demo1.AccountDaoImpl">
        </bean>
        ```

    *   步骤五：在业务层注入DAO，在DAO中注入JDBC模版（强调：简化开发，以后DAO可以继承JdbcDaoSupport类）

        ```xml
        <bean id="accountService" class="com.kkb.demo1.AccountServiceImpl">
        	<property name="accountDao" ref="accountDao"/>
        </bean>
        <bean id="accountDao" class="com.kkb.demo1.AccountDaoImpl">
        	<property name="dataSource" ref="dataSource"/>
        </bean>
        ```

    *   步骤六：编写DAO和Service中的方法

        ```java
        public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {
          public void outMoney(String out, double money) {
            this.getJdbcTemplate().update("update t_account set money = money = ? where name = ?", money,out);
          }
          public void inMoney(String in, double money) {
            this.getJdbcTemplate().update("update t_account set money = money + ? where name = ?", money,in);
          }
        }
        ```

    *   步骤七：编写测试程序.

        ```java
        @RunWith(SpringJUnit4ClassRunner.class)
        @ContextConfiguration("classpath:applicationContext.xml")
        public class Demo1 {
          @Resource(name="accountService")
          private AccountService accountService; 
          @Test
          public void run1(){
          	accountService.pay("冠希", "美美", 1000);
          }
        }
        ```



## 15. Spring 应用之事务支持

#### 一.事务介绍

1.  事务：指的是逻辑上一组操作，组成这个事务的各个执行单元，要么一起成功,要么一起失败！
2.  事务的特性（ACID）

*   原子性（Atomicity）

    原子性是指事务包含的所有操作要么全部成功，要么全部失败回滚。

*  一致性（Consistency）

    一致性是指事务必须使数据库从一个一致性状态变换到另一个一致性状态，也就是说一个事务执行之前和执行之后都必须处于一致性状态。

    拿转账来说，假设用户A和用户B两者的钱加起来一共是5000，那么不管A和B之间如何转账，转几次账，事务结束后两个用户的钱相加起来应该还得是5000，这就是事务的一致性。

*  隔离性（Isolation）

    隔离性是当多个用户并发访问数据库时，比如操作同一张表时，数据库为每一个用户开启的事务，不能被其他事务的操作所干扰，多个并发事务之间要相互隔离。

*  持久性（Durability）

    持久性是指一个事务一旦被提交了，那么对数据库中的数据的改变就是永久性的，即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作。

3.  事务并发问题（隔离性导致）

  在事务的**并发操作**中可能会出现一些问题：

  *   脏读：一个事务读取到另一个事务未提交的数据。
  *   不可重复读：一个事务因读取到另一个事务**已提交的数据**。导致*对同一条记录读取*两次以上的结果不一致。**update**操作
  *   幻读：一个事务因读取到另一个事务**已提交的数据**。导致*对同一张表读取*两次以上的结果不一致。**insert**、delete操作
  
4.  事务隔离级别

*   为了避免上面出现的几种情况，在标准SQL规范中，定义了**4****个事务隔离级别**，不同的隔离级别对事务的处理不同**
  
*   四种隔离级别：
  
    现在来看看MySQL数据库为我们提供的四种隔离级别（*由低到高*）：
  
    *   Read uncommitted (读未提交)：最低级别，任何情况都无法保证。
  *    Read committed (读已提交)：可避免脏读的发生。
    *   Repeatable read (可重复读)：可避免脏读、不可重复读的发生。
  *   Serializable (串行化：可避免脏读、不可重复读、幻读的发生。
  
*   注意事项

    *隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大。*

    对于多数应用程序，可以优先考虑把数据库系统的隔离级别设为Read Committed。它能够避免脏读取，而且具有较好的并发性能。尽管它会导致不可重复读、幻读这些并发问题，在可能出现这类问题的个别场合，可以由应用程序采用悲观锁或乐观锁来控制。

#### 二. Spring 框架的事务管理相关的类和API

**Spring并不直接管理事务，而是提供了多种事务管理器，他们将事务管理的职责委托给Hibernate或者JTA等持久化机制所提供的相关平台框架的事务来实现。 Spring事务管理器的接口是PlatformTransactionManager，通过这个接口，Spring为各个平台如JDBC、Hibernate等都提供了对应的事务管理器，但是具体的实现就是各个平台自己的事情了。**

<img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/JDBC1.png" width = 80% align=left />

1.  **PlatformTransactionManager接口**

    平台事务管理器.(真正管理事务的类)。该接口有具体的实现类，根据不同的持久层框架，需要选择不同的实现类！ 

2.  TransactionDefinition接口：事务定义信息.(事务的隔离级别,传播行为,超时,只读)

3.  TransactionStatus接口：事务的状态（是否新事务、是否已提交、是否有保存点、是否回滚）

4.  总结：上述对象之间的关系：平台事务管理器真正管理事务对象.根据事务定义的信息TransactionDefinition 进行事务管理，在管理事务中产生一些状态.将状态记录到TransactionStatus中

5.  PlatformTransactionManager接口中实现类和常用的方法

    *   接口的实现类

        *   如果使用的Spring的JDBC模板或者MyBatis（IBatis）框架，需要选择**DataSourceTransactionManager**实现类
        *   如果使用的是Hibernate的框架，需要选择**HibernateTransactionManager**实现类

    *   该接口的常用方法

        ```java
        void commit(TransactionStatus status) 
        TransactionStatus getTransaction(TransactionDefinition definition) 
        void rollback(TransactionStatus status) 
        ```

6.  TransactionDefinition

    *   事务隔离级别的常量

        ```java
        static int ISOLATION_DEFAULT
        static int ISOLATION_READ_UNCOMMITTED 
        static int ISOLATION_READ_COMMITTED 
        static int ISOLATION_REPEATABLE_READ 
        static int ISOLATION_SERIALIZABLE 
        ```

    *   事务的传播行为常量（不用设置，使用默认值）

        ```
        * 先解释什么是事务的传播行为：解决的是业务层之间的方法调用！！
        * PROPAGATION_REQUIRED（默认值） -- A中有事务,使用A中的事务.如果没有，B就会开启一个新的事务,将A包含进来.(保证A,B在同一个事务中)，默认值！！
        * PROPAGATION_SUPPORTS          -- A中有事务,使用A中的事务.如果A中没有事务.那么B也不使用事务.
        * PROPAGATION_MANDATORY         -- A中有事务,使用A中的事务.如果A没有事务.抛出异常.
        * PROPAGATION_REQUIRES_NEW      -- A中有事务,将A中的事务挂起.B创建一个新的事务.(保证A,B没有在一个事务中)
        * PROPAGATION_NOT_SUPPORTED     -- A中有事务,将A中的事务挂起.
        * PROPAGATION_NEVER             -- A中有事务,抛出异常.
        * PROPAGATION_NESTED            -- 嵌套事务.当A执行之后,就会在这个位置设置一个保存点.如果B没有问题.执行通过.如果B出现异常,运行客户根据需求回滚(选择回滚到保存点或者是最初始状态
        ```

#### 三. Spring框架事务管理的分类

*   Spring的编程式事务管理（不推荐使用）: 通过手动编写代码的方式完成事务的管理（不推荐）

*   Spring的声明式事务管理（底层采用AOP的技术）:通过一段配置的方式完成事务的管理

    1.  声明式事务管理（重点）
    
        声明式事务管理又分成两种方式
    
        *   基于AspectJ的XML方式（重点掌握）
        *   基于AspectJ的注解方式（重点掌握）

#### 四. 事务管理之基于AspectJ的XML方式（重点掌握）

1.  使用

    准备转账环境：

    *   业务层：（AccountService, AccountServiceImpl）

        ```java
        public void transfer(String in, String out, double money){
          dao.outMoney(out, money);
          // 异常代码
          System.out.print(1/0);
          dao.inMoney(in, money);
        }
        ```

    *   持久层：（AccountDao, AccountDaoImpl）

    *   Spring配置

        <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/JDBC2.png" width = 80% align=left />

    *   单元测试代码

        ```java
        @RunWith(SpringJUnit4ClassRunner.class)
        @ContextConfiguration(locations = "classpath:applicationContext-tx.xml")
        public class TransactionTest {
          @AutoWired
          private AccountService service;
          
          @Test
          public void test01() {
            service.transfer("李聪"， "李阳"， 100);
          }
        }
        ```

    配置事务管理的AOP

    *   平台事务管理器:  DataSourceTransactionManager

        ```xml
        <bean id = "transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
          <property name="dataSource" ref="dataSource"></property>
        </bean>
        ```

    *   事务通知：<<tx:advice id=”” transaction-manager=””/>

        <img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/JDBC3.png" width = 70% align=left />

    *   AOP配置：

        ```xml
        <aop:config>
          <aop:advisor advice-ref=”txAdvice” pointcut=”execution(cn..service.*.*(..))”/>
        </aop:config>
        ```

2.  源码分析

    略

#### 五.事务管理之基于AspectJ的注解方式（重点掌握）

Service 类上或者方法上加注解

*   类上加@Transactional: 表示该类中所有的方法被事务管理
*   方法上加@Transactional：表示只有该方法被事务管理

开启事务注解：

<img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/JDBC4.png" width = 70% align=left />

