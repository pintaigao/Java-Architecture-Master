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

6. 贴心的关闭读取流**inputStream.close()**,并返回含有**Configuration**信息的**DefaultSqlSessionFactory**新对象，也就是返回给**SqlSessionFactory**（第一步）

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

   * 先设置**Environment => this.configuration.getEnvironment()**开发环境，参数都是<environment/>中的参数，什么JDBC啊，Database啊，账户和密码啊等等
   * **TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);** 获取配置环境的事务工程（TransactionFactory），简言之就是获取**JDBC**，
   * **Executor executor = this.configuration.newExecutor(tx, execType);**根据这些配置信息生成执行器对象，细看这个Executor，里面有上面传来的**Transaction**对象
   * 把这些信息中和一下，统一传给**new DefaultSqlSession(this.configuration, executor, autoCommit);**

   这样，openSession完了，作用是解析factory，知道在我们当前的事务中，事务是怎么管理的，数据库是怎么来的，数据库的地址在哪里，以及数据库是怎么连接的，获取这么一个session

   ```json
   Session:
   	configuration: ----> environment information in xml
   	executor: delegate & tcm ---> transaction ---> JDBC & dataSources
   	autoCommit = false // 不让数据库自动提交
   	dirty = false // 还未执行数据库的操作
   ```

8. **session.insert("insertDept",dept)**, 其中insertDept 是sql的id编号,dept是传进sql语句的参数

   ```xml
   <mapper namespace="Dept">
       <!-- 插入单个部门信息 -->
       <insert id="insertDept">
         INSERT INTO dept (DNAME,LOC) VALUES (#{dname},#{loc})
       </insert>
   </mapper>
   ```

   那么问题来了，**insert(id)** 是如何通过这个id来找到这条语句的呢（虽然说他们是通过match一样的id来找到的），这就是**myBatis**的工作，见下

9. myBatis要进行两个工作，1.将sql语句id与sql语句进行定位。2.将数据绑定到sql命令。3.输送sql语句。细看insert这个方法

   ```java
   public int insert(String statement, Object parameter) {
   	return this.update(statement, parameter);
   }
   
   public int update(String statement, Object parameter) {
   	int var4;
   	try {
   		this.dirty = true; // SqlSession的dirty = true
   		MappedStatement ms = this.configuration.getMappedStatement(statement);
   		var4 = this.executor.update(ms, this.wrapCollection(parameter));
   	} catch (Exception var8) {
   		throw ExceptionFactory.wrapException("Error updating database.  Cause: " + var8, var8);
   	} finally {
   		ErrorContext.instance().reset();
   	}
   	return var4;
   }
   ```

   这里的**dirty**是个大学问，但是我们从**MappedStatement ms = this.configuration.getMappedStatement(statement);**开始，这个语句是在找“insertDept”id所对应的sql语句（statement = id），方法是

   ```json
   MappedStatement ms中
   	this.id = "Dept.insertDept"
   	this.resource = "DeptMapper.xml"
   	this.sqlSource --> sql = "INSERT INTO dept (DNAME,LOC) VALUES (?,?)"
   ```

   再看DeptMapper.xml

   ```xml
   <mapper namespace="Dept">
       <!-- 插入单个部门信息 -->
       <insert id="insertDept">
         INSERT INTO dept (DNAME,LOC) VALUES (#{dname},#{loc})
       </insert>
   </mapper>
   ```

   Namespace.id 于是就这么match了，合情合理的对应到<mapper resource = "DeptMapper.xml"/>和id相对应的sql语句，然后带着这些信息，执行**this.executor.update(ms, this.wrapCollection(parameter))**

   ```java
   public int update(MappedStatement ms, Object parameter) throws SQLException {
   	ErrorContext.instance().resource(ms.getResource()).activity("executing an update").object(ms.getId());
   	if (this.closed) {
   		throw new ExecutorException("Executor was closed.");
   	} else {
   		this.clearLocalCache();
   		return this.doUpdate(ms, parameter);
   	}
   }
   ```

   ```java
   public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
   	Statement stmt = null;
   	int var6;
   	try {
   		Configuration configuration = ms.getConfiguration();
   		StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, (ResultHandler)null, (BoundSql)null);
   		stmt = this.prepareStatement(handler, ms.getStatementLog());
   		var6 = handler.update(stmt);
   	} finally {
   		this.closeStatement(stmt);
   	}
   	return var6;
   }
   ```

   注意这条：**StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, (ResultHandler)null, (BoundSql)null)**，反正要知道，这个是在将要传进sql语句的参数和sql语句绑定起来，生成一条完整的插入语句，交还给**StatementHandler**对象，

   **stmt = this.prepareStatement(handler, ms.getStatementLog());**这条又是在干嘛？准备进行数据库连接的操作，因为debug进去看到有**Connection conn JDBC**等对象的调用

   ```java
   private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
   	Connection connection = this.getConnection(statementLog);
   	Statement stmt = handler.prepare(connection);
   	handler.parameterize(stmt);
       return stmt; //com.mysql.jdbc.JDBC4PreparedStatement@4ac3c60d: INSERT INTO dept (DNAME,LOC) VALUES ('国防部','美国')
   }
   ```

   最后**handler.update(stmt);**，将先前准备进行转变，execute，返回将被送进去的数据（一个row）insert，

   **insert 完**

10. 完成session.insert(.....)后，sql语句已经和parameter相结合并封装到Statement对象中，等待输送到数据库中
   ```java
   session.commit();
   
   public void commit(boolean force) {
   	try {
   		this.executor.commit(this.isCommitOrRollbackRequired(force));
   		this.dirty = false;
   	} catch (Exception var6) {
   		throw ExceptionFactory.wrapException("Error committing transaction.  Cause: " + var6, var6);
   	} finally {
   		ErrorContext.instance().reset();
   	}
   }
   ```
   最后在其内部执行**connection.commit()**，进行提交，数据库执行，至于这个commit是具体怎么工作的，那就很深奥了
11. **session.close()** 







   

   

   
