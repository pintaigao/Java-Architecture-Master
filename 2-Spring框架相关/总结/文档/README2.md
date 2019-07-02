# Spring介绍

## 一. 什么是Spring

这个就自己上网查吧



## 二. Spring 核心概念介绍

l  **IoC（核心中的核心）：Inverse of Control，控制反转。**对象的创建权力由程序反转给Spring框架。

l  **AOP：Aspect Oriented Programming，面向切面编程。**在不修改目标对象的源代码情况下，增强IoC容器中Bean的功能。

l  **DI：Dependency Injection，依赖注入。**在Spring框架负责创建Bean对象时，动态的将依赖对象注入到Bean组件中！！

l  **Spring容器：指的就是IoC容器。**



##三.Spring IOC 原理分析

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
     			}
     
     			finally {
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

