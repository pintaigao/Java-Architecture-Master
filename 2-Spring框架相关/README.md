# Spring   框架相关知识

### 基础知识

**Bean**的基础知识:

假设有个这个类

```java
package com.hptg.beans;
public class Teacher {
    private String tname;
	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}
}

```

然后为了让Spring注册这个bean，需要在**java/resources**文件夹下面添加这个文件：spring_config.xml

```xml
<beans>
    <bean id = "teacher" class = "com.hptg.bean.Teacher">
        <property name = "tname" value = "HPTG"/>
    </bean>
</beans>
```

在Main中使用这个bean

```java
ApplicationContext factory = new ClassPathXmlApplicationContext("spring_config.xml");
Teacher teacher = (Teacher) factory.getBean("teacher");
teacher.getTname()...的使用
```



**模拟Bean的工作方法**

1. 创建一个统筹Bean的Class（所有的Bean都符合这个标准）

   ```java
   package com.kaikeba.util;
   public class BeanDefined {
   	private String beanId;
   	private String classPath;
   	public String getBeanId() {
   		return beanId;
   	}
   	public void setBeanId(String beanId) {
   		this.beanId = beanId;
   	}
   	public String getClassPath() {
   		return classPath;
   	}
   	public void setClassPath(String classPath) {
   		this.classPath = classPath;
   	}
   }
   ```

2. 创建一个Bean Factory工厂Class

   ```java
   public class BeanFactory {
       private List<BeanDefined> beanDefinedList;
       public List<BeanDefined> getBeanDefinedList() {
           return beanDefinedList;
       }
   
       public void setBeanDefinedList(List<BeanDefined> beanDefinedList) {
           this.beanDefinedList = beanDefinedList;
       }
   
       // 实现像原生的那样的getBean功能
       public Object getBean(String beanId) throws Exception {
           Object instance;
           for (BeanDefined beanObj : beanDefinedList) {
               if (beanId.equals(beanObj.getBeanId())) {
                   String classPath = beanObj.getClassPath();
                   Class classFile = Class.forName(classPath);
                   //在默认情况下，Spring工厂是通过调用当前类默认工作方法创建实例对象
                   instance = classFile.newInstance();
                   return instance;
               }
           }
           return null;
       }
   }
   ```

3. 执行这堆文件（就像上面那样最终**Teacher teacher = (Teacher) factory.getBean("teacher");**）

   ```java
   public class TestMain {
       public static void main(String[] args) throws Exception {
   
           //1.声明注册bean
           BeanDefined beanObj = new BeanDefined();
           beanObj.setBeanId("teacher");
           beanObj.setClassPath("com.hptg.beans.Teacher");
   
           List beanList = new ArrayList();
           beanList.add(beanObj);//spring核心配置
   
           //2.声明一个Spring提供BeanFacotory
           BeanFactory factory = new BeanFactory();
           factory.setBeanDefinedList(beanList);
   
           //3.开发人员向BeanFactory索要实例对象.
           Teacher t = (Teacher) factory.getBean("teacher");
           System.out.println(t);
       }
   }
   
   ```



scope**属性：

```xml
<bean id = "teacher" class = "com.hptg.bean.Teacher" scope = "prototype" />
```
**scope = singleton：**他所属的类会在Spring容器启动的时候，被创建保存在Spring框架SingletonList，在每次用户调用getBean方法索要时，此时只会返回同一个实例对象

**scope = prototype**：这样的类不会在Spring容器启动时被创建，只有在每次用户调用getBean方法索要时，返回全新实例对象

同样，**模拟**一下这个是怎么工作的：

1. BeanDefined

   ```java
   package com.kaikeba.util;
   
   public class BeanDefined {
   	
   	private String beanId;
   	private String classPath;
   	private String scope ="singleton"; // <---多了一个这个东西
   		
   	public String getScope() {
   		return scope;
   	}
   	public void setScope(String scope) {
   		this.scope = scope;
   	}
   	public String getBeanId() {
   		return beanId;
   	}
   	public void setBeanId(String beanId) {
   		this.beanId = beanId;
   	}
   	public String getClassPath() {
   		return classPath;
   	}
   	public void setClassPath(String classPath) {
   		this.classPath = classPath;
   	}
   }
   ```

2. BeanFactory

   ```java
   public class BeanFactory {
   
       private List<BeanDefined> beanDefinedList;
       private Map<String, Object> SpringIoc;//已经创建好实例对象
   
       public List<BeanDefined> getBeanDefinedList() {
           return beanDefinedList;
       }
   
       public BeanFactory(List<BeanDefined> beanDefinedList) throws Exception {
           this.beanDefinedList = beanDefinedList;
           SpringIoc = new HashMap(); //所有scope="singleton" 采用单类模式管理bean对象
           for (BeanDefined beanObj : this.beanDefinedList) {
               if ("singleton".equals(beanObj.getScope())) {
                   Class classFile = Class.forName(beanObj.getClassPath());
                   Object instance = classFile.newInstance();
                   SpringIoc.put(beanObj.getBeanId(), instance);
               }
           }
       }
   
       public void setBeanDefinedList(List<BeanDefined> beanDefinedList) {
           this.beanDefinedList = beanDefinedList;
       }
   
       public Object getBean(String beanId) throws Exception {
           Object instance = null;
           for (BeanDefined beanObj : beanDefinedList) {
               if (beanId.equals(beanObj.getBeanId())) {
                   String classPath = beanObj.getClassPath();
                   Class classFile = Class.forName(classPath);
                   String scope = beanObj.getScope();
                   if ("prototype".equals(scope)) {//.getBean每次都要返回一个全新实例对象
                       instance = classFile.newInstance();
                   } else {
                       instance = SpringIoc.get(beanId);
                   }
                   return instance;
               }
           }
           return null;
       }
   }	
   ```

3. 在Main中执行这些文件：

   ```java
   public class TestMain {
   	public static void main(String[] args) throws Exception {	
   		  //1.声明注册bean
   		  BeanDefined beanObj = new BeanDefined();
   		  beanObj.setBeanId("teacher");
   		  beanObj.setClassPath("com.kaikeba.beans.Teacher");
   		  beanObj.setScope("prototype"); // 每次生成都将返回一个全新的实例对象
   		  
   		  List beanList = new ArrayList();
   		  beanList.add(beanObj);//spring核心配置
   		  
   		  //2.声明一个Spring提供BeanFacotory
   		  BeanFactory factory = new BeanFactory(beanList);
   		  
   		  //3.开发人员向BeanFactory索要实例对象.
   		  Teacher t= (Teacher) factory.getBean("teacher");
   		  System.out.println("t="+t);
   		  Teacher t2= (Teacher) factory.getBean("teacher");
   		  System.out.println("t2="+t2);
   	}
   }
   ```



**动态工厂 Dynamic Factory：**

如果不用scope的话(默认方式）该怎么创建或不创建独立的instance?

需求：每一个Bean的对象在动态场景中的初始化方式是不一样的

```java
public class TeacherFactory {
    public Teacher createTeacher() {
        Teacher teacher = new Teacher();
        System.out.println("TeacherFactory 负责创建 teacher类实例对象..");
        return teacher;
    }
}
```

然后这个要怎么用呢？当然还是当成一个**Bean**来用：

```xml
 <!-- 在spring_config.xml文件中,Teacher这个值希望是由factory来赋值而不是Spring-->
 <!-- 注册工厂 -->
<bean id="factory1" class="com.hptg.util.TeacherFactory"/>
 <!-- 相当于告诉Spring容器，当前teacher类的实例化操作，由动态工厂来实现-->
<bean id="teacher" factory-bean="factory" factory-method="createTeacher" />
```

同样，执行：

```java
ApplicationContext factory = new ClassPathXmlApplicationContext("spring_config.xml");
Teacher t = (Teacher) factory.getBean("teacher");
```



**改进的factoryBean以及和BeanDefined的相关联**

1. BeanDefined

   ```java
   package com.kaikeba.util;
   
   public class BeanDefined {
       private String beanId;
       private String classPath;
       private String scope = "singleton";
       // 多了以下这两个属性
       private String factoryBean = null;
       private String factoryMethod = null;
   }
   ```

2. BeanFactory

   ```java
   public Object getBean(String beanId) throws Exception {
   	Object instance = null;
   		for (BeanDefined beanObj : beanDefinedList) {
   			if (beanId.equals(beanObj.getBeanId())) {
   				String classPath = beanObj.getClassPath();
   				Class classFile = Class.forName(classPath);
   				String scope = beanObj.getScope();
   				String factoryBean = beanObj.getFactoryBean();
   				String factoryMehtod = beanObj.getFactoryMethod();
   				if ("prototype".equals(scope)) {//.getBean每次都要返回一个全新实例对象
   					if (factoryBean != null && factoryMehtod != null) {
                           //用户希望使用指定工厂创建实例对象
   						Object factoryObj = SpringIoc.get(factoryBean);
   						Class factoryClass = factoryObj.getClass();
   						Method methodObj = factoryClass.getDeclaredMethod(factoryMehtod, null);
   						methodObj.setAccessible(true);
   					instance = methodObj.invoke(factoryObj, null);
   				} else {
   					instance = classFile.newInstance();
   				}
   			} else {
   				instance = SpringIoc.get(beanId);
   			}
   			return instance;
   		}
   	}
   	return null;
   }
   ```

3. 使用

   ```java
   //1.声明注册bean
   BeanDefined beanObj = new BeanDefined();
   beanObj.setBeanId("teacher");
   beanObj.setClassPath("com.hptg.beans.Teacher");
   beanObj.setFactoryBean("factory1");
   beanObj.setFactoryMethod("createTeacher");
   beanObj.setScope("prototype");
   
   // 默认的方法创建实例
   BeanDefined beanObj2 = new BeanDefined();
   beanObj2.setBeanId("factory1");
   beanObj2.setClassPath("com.hptg.beans.TeacherFactory");
   
   List configuration = new ArrayList();
   configuration.add(beanObj);//spring核心配置
   configuration.add(beanObj2);
   
   //2.声明一个Spring提供BeanFacotory
   BeanFactory factory = new BeanFactory(configuration);
   
   //3.开发人员向BeanFactory索要实例对象.
   Teacher t = (Teacher) factory.getBean("teacher");
   System.out.println("t=" + t);
   ```



**静态工厂（为了节省内存）**，一个步骤，就是将TeacherFactory这个class变成static的

```java
//节省内存消耗
public static Teacher createTeacher(){
	Teacher teacher = new Teacher();
	System.out.println("TeacherFactory 负责创建 teacher类实例对象..");
	return teacher;
}
```

并且不需要在spring中单独注册这个bean：将**<bean id = "factory" ... />**删除

然后所关联的实例Bean直接连接到这个工厂中

```xml
<bean id="teacher" class="com.hptg.util.TeacherFactory" factory-method="createTeacher" />
```



**BeanPost工厂**（Bean后处理器，BeanPostProcessor是一个spring写好了的类）

作用：生成Bean对象的代理对象

1. BaseService

   ```java
   package com.hptg.service;
   public interface BaseService {
           public String doSome();
   }
   ```

2. Implement这个Service

   ```java
   package com.hptg.serviceImpl;
   import com.hptg.service.BaseService;
   public class ISomeService implements BaseService {
   	public String doSome() {
   		return "Hello mike";//增强效果，doSome方法返回值都是大写,所以必然要用到代理模式拦截，见下
   	}
   }
   ```

3. Implement BeanPostProcessor

   ```java
   package com.hptg.util;
   public class MyBeanPostProcessor implements BeanPostProcessor {
   
       public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
           System.out.println("bean对象初始化之前。。。。。");
           return bean;
           //return bean对象监控代理对象
       }
   
       public Object postProcessAfterInitialization(final Object beanInstance, String beanName) throws BeansException {
           // 为当前bean对象注册代理监控对象，负责增强bean对象方法能力
           Class beanClass = beanInstance.getClass();
           // 判断他是不是遵循了BaseService原则
           if (beanClass == ISomeService.class) {
               Object proxy = Proxy.newProxyInstance(
                   beanInstance.getClass().getClassLoader(),
                   beanInstance.getClass().getInterfaces(), 
                   new InvocationHandler() {
   					/**
                         * method:doSome args:doSome执行接受实参 proxy:代理监控对对象
                         */
                       public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
   					System.out.println("ISomeService doSome 被拦截");
   					String result = (String) method.invoke(beanInstance, args);// beanInstance.doSome
   					return result.toUpperCase();
   					}
   				});
               return proxy;
           }
           return beanInstance;
       }
   }
   ```

4. 在Spring中注册这个类(spring_config.xml)

   ```xml
   <!-- 注册bean:被监控实现类 -->
   <bean id="isomeService" class="com.kaikeba.serviceImpl.ISomeService"></bean>
   <!-- 注册代理实现类 -->
   <bean class="com.kaikeba.util.MyBeanPostProcessor"></bean>
   ```

   


















### 1. Spring的入门控制反转和AOP



**入门反转（Inversion of control）**



