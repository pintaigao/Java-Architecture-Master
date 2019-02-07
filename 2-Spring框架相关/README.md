# Spring   框架相关知识

### 基础知识

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

* ```xml
  <bean id = "teacher" class = "com.hptg.bean.Teacher" scope = "">
  ```

​	










### 1. Spring的入门控制反转和AOP



**入门反转（Inversion of control）**



