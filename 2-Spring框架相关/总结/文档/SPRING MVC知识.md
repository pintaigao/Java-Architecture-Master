# Spring MVC知识
## 1. 三层结构介绍

我们的开发架构一般都是基于两种形式，一种是 C/S 架构，也就是客户端/服务器，另一种是 **B/S 架构**，也就是浏览器服务器。在 JavaEE 开发中，几乎全都是基于 B/S 架构的开发。那么在 B/S 架构中，系统标准的三层架构包括：**表现层、业务层、持久层**。三层架构在我们的实际开发中使用的非常多，所以我们课程中的案例也都是基于三层架构设计的。

三层架构中，每一层各司其职，接下来我们就说说每层都负责哪些方面： 

*   表现层：
    *   也就是我们常说的web 层。它负责接收客户端请求，向客户端响应结果，通常客户端使用http 协议请求web 层，web 需要接收 http 请求，完成 http 响应。
    *   表现层包括展示层和控制层：控制层负责接收请求，展示层负责结果的展示。
    *   表现层依赖业务层，接收到客户端请求一般会调用业务层进行业务处理，并将处理结果响应给客户端。
    *   表现层的设计一般都使用 MVC 模型。（MVC 是表现层的设计模型，和其他层没有关系）
*   业务层：

    *   也就是我们常说的 service 层。它负责业务逻辑处理，和我们开发项目的需求息息相关。web 层依赖业务层，但是业务层不依赖 web 层。

    *   业务层在业务处理时可能会依赖持久层，如果要对数据持久化需要保证事务一致性。（也就是我们说的， 事务应该放到业务层来控制）
*   持久层：
    *	也就是我们是常说的 dao 层。负责数据持久化，包括数据层即数据库和数据访问层，数据库是对数据进行持久化的载体，数据访问层是业务层和持久层交互的接口，业务层需要通过数据访问层将数据持久化到数据库中。通俗的讲，持久层就是和数据库交互，对数据库表进行曾删改查的。



## 2.MVC设计模式介绍

MVC全名是 Model View Controller，是**模型(model)－视图(view)－控制器(controller)**的缩写， 是一种用于设计创建 Web 应用程序表现层的模式。MVC 中每个部分各司其职：

*   Model（模型）：模型包含业务模型和数据模型，数据模型用于封装数据，业务模型用于处理业务。

*   View（视图）：通常指的就是我们的 jsp 或者 html。作用一般就是展示数据的。通常视图是依据模型数据创建的。

*   Controller（控制器）：是应用程序中处理用户交互的部分。作用一般就是处理程序逻辑的。

    

## 3.SpringMVC介绍

#### 1. Spring MVC是什么？

SpringMVC 是一种基于 Java 的实现 MVC 设计模型的请求驱动类型的轻量级 Web 框架，属于 SpringFrameWork 的后续产品，已经融合在 Spring Web Flow 里面。Spring 框架提供了构建 Web 应用程序的全功能 MVC 模块。使用 Spring 可插入的 MVC 架构，从而在使用 Spring 进行 WEB 开发时，可以选择使用 Spring 的 Spring MVC 框架或集成其他 MVC 开发框架，如 Struts1(现在一般不用)，Struts2 等。

SpringMVC已经成为*目前最主流的 MVC 框架*之一，并且随着Spring3.0 的发布，全面超越 Struts2，成为最优秀的 MVC 框架。

它通过一套注解，让一个简单的 Java 类成为处理请求的控制器，而无须实现任何接口。同时它还支持RESTful 编程风格的请求。

**总之：**

Spring MVC和Struts2一样，都是**为了解决表现层问题**的web框架，它们都是基于MCC设计模式的。而这些表现层框架的主要职责就是**处理前端HTTP请求**

#### 2.SpringMVC如何处理请求

SpringMVC是基于MVC设计模式的，MVC模式指的就是Model（业务模型）、View（视图）、Controller（控制器）。SpringMVC处理请求就是通过MVC这三种角色来实现的。

不过千万不要把MVC设计模式和工程的三层结构混淆，三层结构指的是表现层、业务层、数据持久层。而MVC只针对表现层进行设计。

<img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/MVC1.png" width=80% align=left />

<img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/2-Spring框架相关/总结/文档/图片/MVC2.png" width=80% align=left />

## 开发过程

1.  配置前端控制器：在web.xml中添加DispatcherServlet的配置

    ```xml
    <!-- 前端控制器 -->
    <servlet>
    	<servlet-name>springmvc</servlet-name>
      	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      	<init-param>
      		<param-name>contextConfigLocation</param-name>
      		<param-value>classpath:springmvc.xml</param-value>
      	</init-param>
    </servlet>
    <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <!-- 此处可以多种配置方式： 第一种：*.do，以.do结尾的url进行访问时由DispatcherServlet解析 第二种：/，所有访问的URL都由DispatcherServlet解析，对于静态文件的解析需要配置不让DispatcherServlet进行解析 
    	使用此种方式，可以实现RESTful风格的url 第三种：/*，经测试，这种配置有问题。 如果请求或者通过Controller转发jsp页面时，也会被拦截，此时找不到handler，会报错 -->
    	<url-pattern>*.do</url-pattern>
    </servlet-mapping>
    ```

2.  创建springmvc.xml

3.  编写处理器

4.  