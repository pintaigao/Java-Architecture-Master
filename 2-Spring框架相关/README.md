# Spring   框架相关知识

### 1. 基础知识

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



**scope**属性：

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

并且不需要在spring中单独注册这个bean：将**<bean id = "factory" ... />**  ** <bean id = "factory" ... /> ** 删除

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
   		return "Hello mike";//增强效果，doSome方法如果要返回值都是大写,必然要用到代理模式拦截，见下
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
                       // method:doSome args:doSome执行接受实参 proxy:代理监控对对象
   		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
   			System.out.println("ISomeService doSome 被拦截");
   			String result = (String) method.invoke(beanInstance, args);// beanInstance.doSome
   			return result.toUpperCase();
   		}
   	    }
           );
               return proxy;
           }
           return beanInstance;
       }
   }
   ```

4. 在Spring中注册这个类(spring_config.xml)

   ```xml
   <!-- 注册bean:被监控实现类 -->
   <bean id="isomeService" class="com.hptg.serviceImpl.ISomeService"></bean>
   <!-- 注册代理实现类 -->
   <bean class="com.hptg.util.MyBeanPostProcessor"></bean>
   ```

5. 同样，使用方法：

   ```java
   public class TestMain {
       public static void main(String[] args) {
           ApplicationContext factory = new ClassPathXmlApplicationContext("spring_config.xml");
           BaseService serviceObj = (BaseService) factory.getBean("isomeService");
           System.out.println(serviceObj.doSome());
   
       }
   }
   ```



**Bean初始化，依赖注入（你依赖我这个工厂来为你的对象中的属性进行赋值处理）：**

Spring框架通过反射机制，调用属性对应set方法进行赋值

假设：

```java
public class Teacher {
	private String teacherName;
	private String friendArray[];
	private List<String> school;
    ... 以及这些属性相对应的get和set
}
```

然后，当然还要注册这个Teacher（在spring_config.xml)中：

```xml
<bean id="teacher" class="com.hptg.beans.Teacher">
	<property name="teacherName" value="Mr He"></property>
    <property name="friendArray" value="mike,allen,tom"></property>
    <property name="school" value="长春理工大学，SIT"></property>
</bean>
```

使用：

```java
public static void main(String[] args) {
	ApplicationContext factory = new ClassPathXmlApplicationContext("spring_config.xml");
    Teacher teacher = (Teacher) factory.getBean("teacher");
    // 以下这些都通过Spring然后反射了<bean>中被覆的值 ----> 这就是所谓的Dependency Injection
    System.out.println(teacher.getTeacherName()); 
    System.out.println(teacher.getFriendArray());
   	System.out.println(teacher.getSchool());
}
```

现在来模拟一下这个是怎么运行的：

1. 

   ```java
   public class BeanDefined {
       private String beanId;
       private String classPath;
       private String scope = "singleton";
       private String factoryBean = null;
       private String factoryMethod = null;
       private Map<String, String> propertyMap = new HashMap();
       // 以及他们的get set
   }
   
   ```

2. 先执行，将他们的值放进去：

   ```java
   public class TestMain {
       public static void main(String[] args) throws Exception {
           //1.声明注册bean
           BeanDefined beanObj = new BeanDefined();
           beanObj.setBeanId("teacher");
           beanObj.setClassPath("com.kaikeba.beans.Teacher");
           
           Map<String, String> propertyMap = beanObj.getPropertyMap();
           propertyMap.put("teacherName", "何老师");
   
           List configuration = new ArrayList();
           configuration.add(beanObj);//spring核心配置
   
           //2.声明一个Spring提供BeanFacotory
           BeanFactory factory = new BeanFactory(configuration);
   
           //3.开发人员向BeanFactory索要实例对象.
           Teacher t = (Teacher) factory.getBean("teacher");
           System.out.println("t=" + t);
           System.out.println(t.getTeacherName());
       }
   }
   ```

3. 然后修改Factory文件

   ```java
   public class BeanFactory {
       
       private List<BeanDefined> beanDefinedList;
       private Map<String, Object> SpringIoc;//已经创建好实例对象
       private BeanPostProcessor processorObj;//后置对象
       
       public List<BeanDefined> getBeanDefinedList() {
           return beanDefinedList;
       }
   
       //依赖注入
       public void setValue(Object instance, Class classFile, Map propertyMap) throws NoSuchFieldException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
           //循环便利  propertyMap<属性名,属性值> 
           Method methodArray[] = classFile.getDeclaredMethods();
           Set fieldNameSet = propertyMap.keySet();
           Iterator fieldIterator = fieldNameSet.iterator();
           while (fieldIterator.hasNext()) {
               String fieldName = (String) fieldIterator.next();
               String value = (String) propertyMap.get(fieldName);
               Field fieldObj = classFile.getDeclaredField(fieldName);//同名属性对象
               for (int i = 0; i < methodArray.length; i++) {
                   Method methodObj = methodArray[i];
                   String methodName = "set" + fieldName;// sid == setsid
                   if (methodName.equalsIgnoreCase(methodObj.getName())) {
                       Class fieldType = fieldObj.getType();//属性的数据类型 Integer,String,Double,boolean,list
                       if (fieldType == String.class) {
                           methodObj.invoke(instance, value);
                       } else if (fieldType == Integer.class) {
                           methodObj.invoke(instance, Integer.valueOf(value));
                       } else if (fieldType == Boolean.class) {
                           methodObj.invoke(instance, Boolean.valueOf(value));
                       } else if (fieldType == List.class) {
                           List tempList = new ArrayList();
                           String dataArray[] = value.split(",");
                           for (int j = 0; j < dataArray.length; j++) {
                               tempList.add(dataArray[j]);
                           }
                           methodObj.invoke(instance, tempList);
                       } else { //此时属性类型是数组
                           String dataArray[] = value.split(",");
                           Object data[] = new Object[1];
                           data[0] = dataArray;
                           methodObj.invoke(instance, data);
                       }
                       break;
                   }
               }
           }
       }
       
        public Object getBean(String beanId) throws Exception {
           Object instance = null;
           Object proxyObj = null;//当前实例对象的代理监控对象
           for (BeanDefined beanObj : beanDefinedList) {
               if (beanId.equals(beanObj.getBeanId())) {
                   String classPath = beanObj.getClassPath();
                   Class classFile = Class.forName(classPath);
                   String scope = beanObj.getScope();
                   String factoryBean = beanObj.getFactoryBean();
                   String factoryMehtod = beanObj.getFactoryMethod();
                   Map propertyMap = beanObj.getPropertyMap();
                   if ("prototype".equals(scope)) {//.getBean每次都要返回一个全新实例对象
                       if (factoryBean != null && factoryMehtod != null) {//用户希望使用指定工厂创建实例对象
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
                   if (this.processorObj != null) {
                       proxyObj = this.processorObj.postProcessBeforeInitialization(instance, beanId);
                       //实例对象初始化。Spring依赖注入
                       setValue(instance, classFile, propertyMap);
                       proxyObj = this.processorObj.postProcessAfterInitialization(instance, beanId);
                       //此时返回proxyObj可能就是原始bean对象，也有可能就是代理对象
                       return proxyObj;
                   } else {
                       //实例对象初始化
                       setValue(instance, classFile, propertyMap);
                       return instance;
                   }
               }
           }
           return null;
       }
   }
   
   ```

   

### 2. Spring的AOP(Aspect Oriented Programming 面向切面的编程，用较少的代码实现代理模式)

代理模式实现步骤：

1. 声明接口：注册需要被监听行为名称

2. 接口实现类：扮演被监控的类，负责被监听方法实现细节

3. InnvocationHandler接口实现类：

   1. 次要业务/增强业务
   2. 将次要业务与被监听方法绑定执行

4. 代理监听对象：

   被监控类内存地址，被监控类实现的借口，InvocationHandler实现类的实例对象

**Spring AOP:**简化代理模式实现步骤

1. 声明接口：注册需要被监听的行为名称
2. 接口实现类：扮演被监听的类，负责被监听方法实现细节
3. 次要业务/增强业务

**Spring AOP 通知种类：**设置次要业务与（被监听方法）绑定执行顺序问题

1. 前置通知：

   先：切面：次要业务方法

   后：执行切入点：被拦截的主要业务方法

2. 后置通知：

   先：执行切入点：被拦截的主要业务方法

   后：切面：次要业务方法

3. 环绕通知

   先：切面1:次要业务方法

   然后：执行切入点：被拦截的主要业务方法

   然后：次要业务方法

4. 异常通知

   try{

   ​	执行切入点：被拦截的主要业务方法 

   }catch{

   ​	切面

   }



相关代码：

连接点：

```java
public interface BaseService {
	public void eat();//JoinCut 连接点
	public void wc();//JoinCut 连接点
}
```

实现以上的叫切入点：

```java
public class Person implements BaseService {
    public void eat() {//切入点 PointCut  主要业务方法
        System.out.println("吃泡面");
    }

    public void wc() {//切入点 PointCut   主要业务方法
        System.out.println("上厕所");
    }
}
```

接下来，切面（通知）：

```java
/*
 *
 *   public class Agent implements InvocationHandler{
 *
 *      private BaseService obj;//当前具体被监控对象
 *
 *       public Agent(BasseSercie param){
 *          this.obj = param;
 *      }
 *
 *      public Object invoke(Object proxy,Method method,Object[] args){
 *             //织入顺序
 *      }
 *
 *      //次要业务
 *      public wash(){
 *      }
 *   }
 * */
public class MyBeforeAdvice implements MethodBeforeAdvice {
    //切面：次要业务（arg0:被拦截的方法）
    public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
        System.out.println("-----洗手-----");
        ProxyFactoryBean cc;
    }
}

```

接下来，注册：

```xml
<!-- 注册被监控实现类 -->
<bean id="person" class="com.kaikeba.serviceImpl.Person"></bean>

<!-- 注册通知实现类 -->
<bean id="before" class="com.kaikeba.advice.MyBeforeAdvice"></bean>

<!-- 注册 代理监控对象的生产工厂 -->
<bean id="personProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
    <!-- target：被拦截的对象，interceptorNames：拦截器的名字，指向通知 -->
	<property name="target" ref="person"></property>
	<property name="interceptorNames">
		<array>
			<value>before</value>
		</array>
	</property>
</bean>
```

和之前的那个洗手吃饭实现方式相比，是不是简介了很多，

接着，执行（P.s:getBean反悔的不再是Person，而是Proxy返回的Person代理对象personProxy）

```java
public class TestMain {
    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext("spring_config.xml");
        BaseService personProxy = (BaseService) factory.getBean("personProxy");
        personProxy.eat();// 洗手  吃饭
        personProxy.wc(); //  上厕所
    }
}
```

它会输出：

```
--洗手--
吃泡面
--洗手--
上厕所
```











