SSM三大框架整合:
#### 需求

* 查询所有帐号，显示到列表页面上
* 使用SSM三大框架完成功能

#### 说明

* 整合说明：SSM整合有多种方式，我们选择XML+注解的方式

* 整合思路：

  * 先准备整合环境
  * 搭建Spring环境
  * 搭建Mybatis环境，然后整合到Spring中
  * 搭建SpringMVC环境，然后整合到Spring中

* 数据库表结构：

  ```mysql
  drop database if exists ssm;
  create database ssm;
  use ssm;
  create table account(
  	id int primary key auto_increment,
  	name varchar(20),
  	money double
  );
  insert into `account` (`id`, `name`, `money`) values('1','tom','1000');
  insert into `account` (`id`, `name`, `money`) values('2','jerry','1000');
  insert into `account` (`id`, `name`, `money`) values('3','jack','1500');
  insert into `account` (`id`, `name`, `money`) values('4','rose','1500');
  
  create table user(
  	id int primary key auto_increment,
      username varchar(32),
      password varchar(64)
  );
  
  insert into user(username, password) values ('admin', 'admin');
  ```

#### 步骤

1. 创建maven的web项目
   * 补充项目需要的文件夹java、resources等
2. 导入jar依赖：
   * 在`pom.xml`里导入jar包依赖
3. 创建页面和package：
   * 页面：
     * 在webapp下创建文件夹pages，在pages里创建`list.jsp`，用于显示帐号列表
   * package：
     * 准备4个包
       * `com.itheima.controller`
       * `com.itheima.service`
       * `com.itheima.dao`
       * `com.itheima.pojo`
     * 在`com.itheima.pojo`包里创建JavaBean：`Account`
4. 准备`log4j.properties`

#### 实现

##### 1. 创建maven的web项目

* 目录结构如下：

![image-20200413112146245](img/image-20200413112146245-1594390115847.png)

##### 2. 导入jar包依赖

```xml
<properties>
    <!-- 版本锁定 -->
    <spring.version>5.0.2.RELEASE</spring.version>
    <log4j.version>1.2.17</log4j.version>
    <slf4j.version>1.6.6</slf4j.version>
    <mysql.version>5.1.47</mysql.version>
    <mybatis.version>3.4.6</mybatis.version>
</properties>

<dependencies>
    <!--Spring：ioc和aop，事务-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>1.8.9</version>
    </dependency>

    <!--Mybatis-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>${mysql.version}</version>
    </dependency>
    <dependency>
        <groupId>com.mchange</groupId>
        <artifactId>c3p0</artifactId>
        <version>0.9.5.4</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>${mybatis.version}</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>2.0.2</version>
    </dependency>

    <!--SpringMVC，jstl，json转换-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.9.9</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>
        <version>4.0.1</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>javax.servlet.jsp-api</artifactId>
        <version>2.3.1</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>jstl</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>

    <!--日志-->
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>${log4j.version}</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>${slf4j.version}</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>${slf4j.version}</version>
    </dependency>

    <!--测试-->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId>
        <version>${spring.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

##### 3. 创建页面和package

- 在webapp下创建文件夹`pages`，在`pages`文件夹里创建`list.jsp`，用于显示帐号列表

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>帐号列表</title>
</head>
<body>
<table border="1" width="500" align="center">
    <tr>
        <th>序号</th>
        <th>帐号</th>
        <th>金额</th>
        <th>管理</th>
    </tr>
    <tr>
        <td>1</td>
        <td>张三</td>
        <td>300</td>
        <td>
            <a href="#">删除</a>
        </td>
    </tr>
</table>
</body>
</html>
```

- 创建package，并在pojo里创建JavaBean

```java
package com.itheima.pojo;

public class Account {
    private Integer id;
    private String name;
    private Double money;

    //get/set...
    //toString...
}

```

##### 4. 准备`log4j.properties`

* 在`resources`里创建文件`log4j.properties`

```properties
# Set root category priority to INFO and its only appender to CONSOLE.
#log4j.rootCategory=INFO, CONSOLE            debug   info   warn error fatal
log4j.rootCategory=info, CONSOLE

# CONSOLE is set to be a ConsoleAppender using a PatternLayout.
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{ISO8601} %-6r [%15.15t] %-5p %30.30c %x - %m\n
```

### 2. 准备Spring环境

#### 目标

* 准备Spring的配置，确定Spring环境配置正常
* 是service层的配置

#### 说明

* Spring框架扫描注解，不扫描`@Controller`注解
* `@Controller`交给SpringMVC管理

#### 步骤

1. 创建`AccountService`接口和实现类
   * 在`AccountServiceImpl`上增加注解`@Service`，配置成bean对象
2. 创建`spring-service.xml`配置文件，开启组件扫描
   * 不扫描`@controller`注解（要交给SpringMVC来处理）
3. 功能测试：
   * 编写测试类，确认能否通过Spring容器获取`AccountServiceImpl`对象

#### 实现

##### 1. 创建`AccountService`接口和实现类

* 创建`AccountService`接口

```java
public interface AccountService {
    List<Account> queryAll();
}
```

* 创建实现类`AccountServiceImpl`，并在类上增加注解`@Service`

```java
@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Override
    public List<Account> queryAll() {
        System.out.println("AccountServiceImpl.queryAll()");
        return null;
    }
}
```

##### 2. 创建`spring-service.xml`配置文件，开启注解扫描

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd
	http://www.springframework.org/schema/aop
	http://www.springframework.org/schema/aop/spring-aop.xsd
	http://www.springframework.org/schema/tx
	http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--开启注解扫描-->
    <context:component-scan base-package="com.itheima">
        <!--不扫描Controller注解（Controller注解由SpringMVC负责处理）-->
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

</beans>
```

##### 3. 创建一个单元测试类，测试Spring环境配置是否成功

```java
/**
 * @author liuyp
 * @date 2020/10/31
 */
public class SsmTest {

    @Test
    public void testService(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring-service.xml");
        AccountService accountService = app.getBean(AccountService.class);
        accountService.queryAll();
    }
}
```

### 3. 准备Mybatis环境，整合到Spring中

#### 1. 准备Mybatis环境

##### 说明

- 使用Mybatis实现dao层操作数据库
- 我们这里使用的是注解方式

##### 步骤

1. 创建`AccountDao`接口，并创建`queryAll方法：用注解的方式
2. 创建`sqlMapConfig.xml`配置文件
3. 功能测试：否查询得到所有Account

##### 实现

###### 1. 在`AccountDao`的方法上增加注解，实现`queryAll`和`save`功能

```java
public interface AccountDao {
    @Select("select * from account")
    List<Account> queryAll();
}
```

###### 2. 创建`sqlMapConfig.xml`配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--配置别名-->
    <typeAliases>
        <package name="com.itheima.pojo"/>
    </typeAliases>

    <!--配置数据库环境-->
    <environments default="ssm">
        <environment id="ssm">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql:///ssm"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>

    <!--配置映射器扫描-->
    <mappers>
        <package name="com.itheima.dao"/>
    </mappers>
</configuration>
```

###### 3. 功能测试

* 在测试类`SSMTest`里增加方法

```java
    @Test
    public void testDao() throws IOException {
        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = factory.openSession();
        AccountDao dao = session.getMapper(AccountDao.class);
        List<Account> accounts = dao.queryAll();
        for (Account account : accounts) {
            System.out.println(account);
        }
        session.close();
        is.close();
    }
```

#### 2. 把Mybatis整合到Spring中

##### 说明

- dao层映射器代理对象，由Spring来创建和管理

##### 步骤

1. 创建`spring-dao.xml`，增加Mybatis的配置支持

2. 功能测试：能否通过Spring容器，直接得到dao层映射器的代理对象


##### 实现

###### 创建`spring-dao.xml`，增加Mybatis的配置支持

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!--配置连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql:///ssm"/>
        <property name="user" value="root"/>
        <property name="password" value="root"/>
    </bean>

    <!--配置SqlSessionFactoryBean：会由Spring创建SqlSessionFactory对象-->
    <bean class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="typeAliasesPackage" value="com.itheima.pojo"/>
        <!--<property name="configLocation" value="classpath:sqlMapConfig.xml"/>-->
    </bean>

    <!--配置映射器扫描-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.itheima.dao"/>
    </bean>
</beans>
```

###### 功能测试

* 修改测试类`SSMTest`：

```java
 	@Test
    public void testSpringDao(){
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring-dao.xml");
        AccountDao dao = app.getBean(AccountDao.class);
        List<Account> accounts = dao.queryAll();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }
```

### 4. 准备SpringMVC环境，整合到Spring中

#### 1. 准备SpringMVC环境

##### 说明

* SpringMVC只扫描`@Controller`注解，其它的组件注解交给Spring扫描

##### 步骤

1. 创建`AccountController`类
2. 创建`spring-web.xml`配置文件
3. 修改`web.xml`
   * 配置前端控制器`DispatcherServlet`
   * 配置编码过滤器`CharacterEncodingFilter`
4. 部署测试

##### 实现

###### 修改`AccountController`类，配置注解

```java
@Controller
@RequestMapping("/account")
public class AccountController {

    @RequestMapping("/queryAll")
    public String queryAll(){
        System.out.println("queryAll方法执行了");
        return "list";
    }
}
```

###### 创建`spring-web.xml`配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd
	http://www.springframework.org/schema/mvc
	http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--开启注解扫描，只扫描Controller注解-->
    <context:component-scan base-package="com.itheima">
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

    <!--开启mvc的注解驱动-->
    <mvc:annotation-driven/>

    <!--配置视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/pages/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <!--处理静态资源-->
    <mvc:default-servlet-handler/>
</beans>
```

###### 修改`web.xml`

- 配置前端控制器`DispatcherServlet`
- 配置乱码解决的过滤器`CharacterEncodingFilter`

```xml
<!--SpringMVC：配置乱码过滤器-->
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>utf-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>

<!--SpringMVC：配置前端控制器-->
<servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-web.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

###### 部署测试

* 把web项目部署到Tomcat，启动服务
* 客户端访问：`http://localhost:8080/ssm/account/queryAll`

#### 2. 整合SpringMVC和Spring

##### 说明

* web层的Controller已经可以被客户端访问了，但是web层依赖于service层，需要注入依赖

##### 步骤

1. 修改`AccountController`
   * 把Service注入给Controller
   * 在`queryAll`方法中：
     * 调用service的`queryAll`方法，得到`List<Account>`
     * 把`List<Account>`放到Model中，请求转发到`/pages/list.jsp`
2. 修改`AccountServiceImpl`
   * 把`AccountDao`注入给service
   * 在`queryAll`方法中调用accountDao，得到`List<Account>`
3. 修改`web.xml`，在服务器启动时就加载Spring的配置文件
4. 部署web项目，启动测试

##### 实现

###### 修改`AccountController`

* 注入`accountService`
* 修改`queryAll`方法

```java
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/queryAll")
    public String queryAll(Model model){
        List<Account> accounts = accountService.queryAll();
        model.addAttribute("accounts", accounts);
        return "list";
    }
}
```

###### 修改`AccountServiceImpl`

* 注入依赖`AccountDao`
* 调用`AccountDao`查询所有帐号

```java
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public List<Account> queryAll() {
        return accountDao.queryAll();
    }
}
```

###### 修改`list.jsp`

* 从request域中取出数据，使用JSTL和EL循环显示到页面上

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>帐号列表</title>
</head>
<body>
<table border="1" width="500" align="center">
    <tr>
        <th>序号</th>
        <th>帐号</th>
        <th>金额</th>
        <th>管理</th>
    </tr>
    <c:forEach items="${accounts}" var="account" varStatus="status">
        <tr>
            <td>${status.count}</td>
            <td>${account.name}</td>
            <td>${account.money}</td>
            <td>
                <a href="#">删除</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
```

###### 修改`web.xml`，在服务器启动时就加载Spring的配置文件

```xml
<!--配置前端控制器-->
<servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <!-- 加载所有spring配置文件 -->
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-*.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

###### 部署web项目，启动测试

* 客户端访问：`http://localhost:8080/ssm/account/queryAll`

### 5. 添加事务管理

#### 说明

- Service层需要事务控制，在Spring的配置文件中配置事务管理

#### 步骤

- 直接修改`spring-service.xml`，增加事务管理的配置即可

#### 实现

```xml
<!--事务管理器-->
<bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
<!--事务通知-->
<tx:advice id="txAdvice" transaction-manager="txManager">
    <tx:attributes>
        <tx:method name="*"/>        
        <tx:method name="query*" read-only="true"/>
        <tx:method name="find*" read-only="true"/>
    </tx:attributes>
</tx:advice>
<!--配置切面-->
<aop:config>
    <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.itheima.service..*.*(..))"/>
</aop:config>
```

### 6. 练习：完成添加帐号功能

#### 需求

* 完成添加帐号功能
* 帐号添加成功后，跳转到列表页面，显示最新的帐号列表

#### 分析

##### 功能分析

* 需要创建一个页面，页面里提供表单
* 服务端要提供功能：接收帐户信息，并把帐户信息插入到数据库里
* 添加成功后，不能直接到list.jsp，要跳转到`/account/queryAll`

##### 实现步骤

1. 创建页面：
   * 在`pages`文件夹里创建页面`add.jsp`
   * `add.jsp`页面提交表单到`AccountController`
2. 服务端接收数据并保存到数据库
   1. `AccountController`调用`AccountServiceImpl`
   2. `AccountServiceImpl`调用`AccountDao`
   3. `AccountDao`把`Account`的数据插入到数据库里
   4. `AccountController`跳转到`/account/queryAll`
3. 部署测试

#### 实现

##### 1. 页面提交表单到`AccountController`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加帐号</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/account/add" method="post">
    帐户名：<input type="text" name="name"><br>
    余额：<input type="text" name="money"><br>
    <input type="submit" value="添加帐号">
</form>
</body>
</html>
```

##### 2. `AccountController`接收数据得到`Account`对象

###### `AccountController`

* 在`AccountController`里增加方法

```java
    @RequestMapping("/add")
    public String add(Account account){
        accountService.add(account);
        return "redirect:/account/queryAll";
    }
```

###### `AccountService`

* `AccountService`接口里增加方法

```java
	void add(Account account);
```

* `AccountServiceImpl`类里实现方法

```java
    @Override
    public void add(Account account) {
        accountDao.add(account);
    }
```

###### `AccountDao`

* 在`AccountDao`里增加方法，并配置SQL语句

```java
    @Insert("insert into account(name,money) values(#{name},#{money})")
    void add(Account account);
```

##### 3. 部署测试

* 把web项目部署到Tomcat，启动服务
* 用户输入数据，提交表单

### 7. 注意事项

* 在SpringMVC框架使用中，如果**jsp页面上**要加载资源文件，路径**建议使用绝对路径**
  * 因为SpringMVC里有大量的请求转发，资源文件的相对位置会发生变化。使用绝对路径能够保证不出问题
  * `${pageContext.request.contextPath}/pages/mm.jpg`

### 8. 拦截器实现登录权限控制

#### 需求说明：

* 有登录功能
* 登录权限控制的拦截器

#### 需求分析：

* 登录功能：
  * 需要一张表`user`（《1. 环境准备》中已经建表），表里要有用户名和密码
  * 需要创建JavaBean：`User`
  * 提供登录的功能：
    * 如果登录成功了，跳转到`/account/queryAll`
* 登录权限控制的拦截器
  * 访问服务端的`/account/**`资源时：
    * 如果是已登录，就放行；如果是未登录，就跳转到登录页面

#### 需求实现

##### 登录功能

* 在`pages`文件夹里创建登录页面`login.jsp`

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/user/login" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    <input type="submit" value="登录">
</form>
</body>
</html>
```

* 创建JavaBean：`User`

```java
public class User {
    private Integer id;
    private String username;
    private String password;

    //get/set...
    //toString...
}
```

* 创建`UserController`

```java
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(User loginUser, HttpSession session){
        User user = userService.login(loginUser);
        session.setAttribute("user", user);
        return "redirect:/account/queryAll";
    }
}
```

* 创建`UserService`接口和实现类

```java
public interface UserService {
    User login(User loginUser);
}
```

```java
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User login(User loginUser) {
        return userDao.login(loginUser);
    }
}
```

* 创建`UserDao`接口

```java
public interface UserDao {
    @Select("select * from user where username=#{username} and password=#{password}")
    User login(User loginUser);
}
```

##### 登录权限控制

* 在`com.itheima.interceptors`包里创建拦截器

```java
public class PriviligesInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            System.out.println("未登录，即将跳转到登录页面");
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return false;
        }else{
            return true;
        }
    }
}
```

* 修改`spring-web.xml`，配置拦截器

```xml
    <!--配置拦截器-->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/account/**"/>
            <bean class="com.itheima.interceptor.PriviligesInterceptor" />
        </mvc:interceptor>
    </mvc:interceptors>
```

