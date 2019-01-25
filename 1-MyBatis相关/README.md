# MyBatis 相关知识

### 1.MyBatis的工作流程

初始代码：

```java
Dept dept = new Dept();
dept.setDname("国防部");
dept.setLoc("美国");
InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
SqlSession session = factory.openSession();
session.insert("insertDept",dept);
session.commit();
session.close();
```

以及相对应的配置**mybatis-config.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE configuration
    PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <properties resource="config.properties" />
    <typeAliases>
        <package name="com.hptg.application" />
    </typeAliases>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC" />
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}" />
                <property name="url" value="${jdbc.url}" />
                <property name="username" value="${jdbc.username}" />
                <property name="password" value="${jdbc.password}" />
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="DeptMapper.xml"/>
    </mappers>


</configuration>
```

以及**DeptMapper.xml**

```xml
<mapper namespace="Dept">
    <!-- 插入单个部门信息 -->
    <insert id="insertDept">
      INSERT INTO dept (DNAME,LOC) VALUES (#{dname},#{loc})
    </insert>
</mapper>
```

好现在来看是怎么工作的：

1. **InputStream** 将mybatis-config.xml变成文件流，代写入

2. 进入**SqlSessionFactoryBuilder**并执行**build**的重载方法

   ```java
   build((InputStream)inputStream, (String)null, (Properties)null)
   ```

3. 将传进来的参数放到**XMLConfigBuilder** parser中，



