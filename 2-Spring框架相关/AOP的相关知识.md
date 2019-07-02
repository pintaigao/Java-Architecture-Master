# Spring AOP

### 什么是AOP：

​	在软件业，AOP为Aspect Oriented Programming的缩写，意为：面向**切面**编程， AOP是一种编程范式，隶属于软工范畴，指导开发者如何组织程序结构，AOP最早由AOP联盟的组织提出的,制定了一套规范.Spring将AOP思想引入到框架中,必须遵守AOP联盟的规范，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术， AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型，利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

### AOP的作用及优势

* 作用：
  *  AOP采取横向抽取机制，取代了传统纵向继承体系重复性代码（性能监视、事务管理、安全检查、缓存）
  * 在程序运行期间，不修改源码对已有方法进行增强。
  * 业务逻辑和系统处理的代码（关闭连接、事务管理、操作日志记录）解耦。**

* 优势：
  * 减少重复代码
  * 提高开发效率
  * 维护方便



### AspectJ的实现

```java
package com.kkb.spring.utils;
public class MyProxyUtils {
    /**
     * 使用JDK的动态代理实现 它是基于接口实现的
     *
     * @param serviceImpl
     * @return
     */
    public static UserService getProxy(UserService service) {
        // Proxy是JDK中的API类
        // 第一个参数：目标对象的类加载器
        // 第二个参数：目标对象的接口
        // 第三个参数：代理对象的执行处理器（匿名内部类）
        UserService userService = (UserService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("记录日志-开始");
                        // 下面的代码，是反射中的API用法
                        // 该行代码，实际调用的是目标对象的方法
                        Object object = method.invoke(service, args);
                        // 调用的过程中，做一些其他操作
                        System.out.println("记录日志-结束");
                        return object;
                    }
                });

        return userService;
    }

    /**
     * 使用CGLib动态代理技术实现 它是基于继承的方式实现的
     *
     * @param service
     * @return
     */
    public static UserService getProxyByCgLib(UserService service) {

        // 创建增强器
        Enhancer enhancer = new Enhancer();
        // 设置需要增强的类的类对象
        enhancer.setSuperclass(UserServiceImpl.class);

        // 设置回调函数
        enhancer.setCallback(new MethodInterceptor() {
					// MethodProxy：代理之后的对象的方法引用
					@Override
					public Object intercept(Object object, Method method, Object[] arg, MethodProxy methodProxy)throws Throwable {
                long start = System.currentTimeMillis();
                System.out.println("记录程序开始时间..." + start);

                // 因为代理对象是目标对象的子类
                // 该行代码，实际调用的是目标对象的方法
                // object :代理对象
                Object object2 = methodProxy.invokeSuper(object, arg);

                long end = System.currentTimeMillis();
                System.out.println("记录程序结束时间..." + end);
                System.out.println("记录程序执行总时长..." + (end - start));
                return object2;
            }
        });

        // 获取增强之后的代理对象
        UserService userService = (UserService) enhancer.create();
        return userService;
    }
}
```

调用这些个类：

```java
@Test
public void testJDKProxy() {
	//创建目标对象
	UserService service = new UserServiceImpl();	
	//生成代理对象
  UserService proxy = MyProxyUtils.getProxy(service);	
	//调用目标对象的方法
  service.saveUser();
	System.out.println("===============");
  //调用代理对象的方法
	proxy.saveUser();
}
@Test
public void testCgLibProxy() {
  //创建目标对象
	UserService service = new UserServiceImpl();
  //生成代理对象
	UserService proxy = MyProxyUtils.getProxyByCgLib(service);	

  //调用目标对象的方法	
  service.saveUser();	
	System.out.println("===============");
	//调用代理对象的方法
	proxy.saveUser();
}
```



### Spring 自己的AOP方法

配置applicationContext.xml

```xml
<!-- 配置目标对象 -->
<bean class="com.kkb.spring.service.UserServiceImpl"></bean>
<!-- 配置通知类 -->
<bean id="myAdvice" class="com.kkb.spring.advice.MyAdvice"></bean>

<!-- AOP配置 -->
<aop:config>
<!-- 配置AOP切面，切面是由通知和切入点组成的 -->
	<!--<aop:advisor advice-ref="" pointcut=""/>-->
  <aop:aspect ref="myAdvice">
	  <!-- before：前置通知 -->
  	<!-- pointcut:编写切入点表达式 ,去定位需要切入的方法是哪个 -->
	  <!-- method：增强类中的方法 -->
  	<aop:before method="log" pointcut="execution(void com.kkb.spring.service.UserServiceImpl.saveUser())"/>
		<aop:after-returning method="log2" pointcut="execution(void com.kkb.spring.service.UserServiceImpl.saveUser())"/>
    
  	<!--after-returning 之后 after 最终通知 -->
	  <aop:after method="log3" pointcut="execution(void com.kkb.spring.service.UserServiceImpl.saveUser())"/>
	 	<aop:after-throwing method="log4" pointcut="execution(void com.kkb.spring.service.UserServiceImpl.saveUser())"/>
		<!--around:环绕通知-->
  	<aop:around method="log5" pointcut="execution(void *..*.*ServiceImpl.*(..))" />
  	<!-- pointcut="execution(void com.kkb.spring.service.UserService.saveUser())" /> -->
  </aop:aspect>
</aop:config>

```

> 这一段中<aop:before 那个就是：log方法切入进 切入点saveUser（）这个方法，增强这个方法，执行saveUser（）这个方法的同时才执行log（）方法

通知：

```java
package com.kkb.spring.advice;
public class MyAdvice {
    //演示前置通知
    public void log() {
        System.out.println("开始记录日志了...");
    }

    //演示后置通知
    public void log2() {
        System.out.println("开始记录日志了log2...");
    }

    //演示最终通知
    public void log3() {
        System.out.println("开始记录日志了log3...");
    }

    //演示异常抛出通知
    public void log4() {
        System.out.println("开始记录日志了log4...");
    }

    /**
     * 环绕通知
     * 场景使用：事务管理
     *
     * @param joinPoint
     * @throws Throwable
     */
    public void log5(ProceedingJoinPoint joinPoint) {
        System.out.println("前置通知");
        //调用目标对象的方法
        try {
            joinPoint.proceed();
            System.out.println("后置通知");
        } catch (Throwable e) {
            //相当于实现异常通知
            System.out.println("异常抛出配置");
            e.printStackTrace();
        } finally {
            System.out.println("最终通知");
        }
    }
}

```

### 使用注解支持AOP

```java
package com.kkb.spring.aspect;
//@Aspect:标记该类是一个切面类
@Aspect
// 需要将切面类交给Spring IoC容器管理
@Component("myAspect")
public class MyAspect {
    //切入点表达式
    private static final String str = "execution(* *..*.*ServiceImpl.*(..))";
    // @Before：定义该方法是一个前置通知
    @Before(value = "execution(* *..*.*ServiceImpl.*(..))")
    public void before() {
        System.out.println("注解前置通知");
    }

    @After(value = "execution(* *..*.*ServiceImpl.*(..))")
    public void after() {
        System.out.println("注解最终通知");
    }

    @Before(value = "fn()")
    public void before() {
        System.out.println("注解前置通知");
    }

    @After(value = "fn()")
    public void after() {
        System.out.println("注解最终通知");
    }
  
    @AfterReturning(value = "fn()")
    public void afterReturning() {
        System.out.println("注解最终后返回通知");
    }

    //使用PointCut注解，来定义一个通用切入点表达式
    @Pointcut(value = str)
    public void fn() {
    }
}

```

