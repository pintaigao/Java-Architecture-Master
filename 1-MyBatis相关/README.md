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

3. 将传进来的参数放到build方法中的新**XMLConfigBuilder** parser  实例中，并执行**build(parser.parse())**

4. 在**parse**这个方法中，由this.parseConfiguration解析传来的configure.xml数据流，并返回**this.configuration**,里面包含了configure.xml中的<environment /> 等的一些参数信息（其实就是返回了一个新的**Configuration**对象)

5. 执行**build(新对象)**，同样这个是一个重构对象

   ```java
   public SqlSessionFactory build(Configuration config) {
   	return new DefaultSqlSessionFactory(config);
   }
   ```

6. 贴心的关闭读取流**inputStream.close()**,并返回含有**Configuration**信息的**DefaultSqlSessionFactory**，也就是返回给**SqlSessionFactory**（第一步）

7. 到

   ```java
   SqlSession session = factory.openSession();
   // 以及
   public SqlSession openSession() {
       return this.openSessionFromDataSource(this.configuration.getDefaultExecutorType(), (TransactionIsolationLevel)null, false);
   }
   ```

   进入

   ```java
   this.configuration.getDefaultExecutorType()
   ```

   并返回**defaultExecutorType**，这个就超简单了，里面就是一个“Simple”，再回到**openSessionFromDataSource（）**

   ```java
   private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
       Transaction tx = null;
       DefaultSqlSession var8;
       try {
         Environment environment = this.configuration.getEnvironment();
         TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);
         tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
         Executor executor = this.configuration.newExecutor(tx, execType);
         var8 = new DefaultSqlSession(this.configuration, executor, autoCommit);
       } catch (Exception var12) {
         this.closeTransaction(tx);
         throw ExceptionFactory.wrapException("Error opening session.  Cause: " + var12, var12);
       } finally {
         ErrorContext.instance().reset();
       }
   
       return var8;
   }
   ```

   

   

