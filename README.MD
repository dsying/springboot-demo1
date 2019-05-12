# spring核心观念

## bean 咖啡豆
提供服务的对象，spring中的最小单位

### 配置bean的方式
+ Loading Beans from XML Configuration

src/main/resources/applicationContext.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <bean id="userService" class="hello.service.UserService">
    </bean>
    <bean id="orderService" class="hello.service.OrderService">
    </bean>
</beans>
```
创建一个configuration class 去加载这个 定义bean的xml文件
```java
@Configuration
// applicationContext.xml will be imported from classpath.
@ImportResource({"classpath*:applicationContext.xml"})
public class XmlConfiguration {
}
```

+ @annotation 注解的方式
```java
@Configuration
public class JavaConfiguration {

    @Bean
    public UserService userService(){
        return new UserService();
    }
}
```

## IOC 控制反转
- spring创建bean,并维护bean
- 不使用spring时，通过 new的方式创建 bean
- 使用了spring后，可以通过注解的方式注入bean

## DI 依赖注入
+ @Resource （历史）
+ @Autowired （历史）
+ @Inject （推荐 使用）

 ### 使用构造器注入
 - 优先使用构造器注入bean
 
 Always use constructor based dependency injection in your beans. Always use assertions for mandatory dependencies
 ```java
import javax.inject.Inject;
@Inject
public HelloController(OrderService orderService) {
    this.orderService = orderService;
}
```

# docker
## docker容器中使用mysql

### 从docker hub的仓库中拉取mysql镜像 
```bash
sudo docker pull mysql
```
### 运行一个mysql容器
```bash
docker run --name docker_mysql_1 -e MYSQL_ROOT_PASSWORD=19920115asd -e MYSQL_DATABASE=dsying -p 3306:3306 -d mysql mysqld --default-authentication-plugin=mysql_native_password
```
上述命令各个参数含义：
```
docker run      :运行一个容器 使用mysql这个镜像创建一个名为 mysql1 的 容器 
--name          :后面是这个镜像的名称
-e MYSQL_ROOT_PASSWORD=my-secret-pw :指定root用户密码
-e MYSQL_DATABASE=xdml  :指定数据库名
-p 3306:3306    :将宿主(本地)端口 和 容器端口 绑定
-d              :表示使用守护进程运行，即服务挂在后台
mysqld --default-authentication-plugin=mysql_native_password  :新版本的MySQL使用caching_sha2_password而不是mysql_native_password
```

# springboot 连接mybatis
## 创建datasource
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dsying?characterEncoding=utf8&useSSL=false
spring.datasource.username=root
spring.datasource.password=19920115asd
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```
## 创建一个mapper并通过 @Component生成bean
```java
@Mapper
@Component
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(@Param("id") Integer id);
}
```
## 注入mapper的 bean
```java
public class UserService {
    UserMapper userMapper;

    @Inject
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }
}
```


## sql脚本
```sql
create table user
(
    id                 int auto_increment
        primary key,
    username           varchar(10)  null,
    encrypted_password varchar(100) null,
    avatar             varchar(100) null,
    created_at         datetime     null,
    updated_at         datetime     null,
    constraint user_username_uindex
        unique (username)
);
```

## FlyWay 数据库迁移自动化
**flyway会在目标数据库中 自动执行 约定目录下的 sql脚本**

### 创建sql脚本(约定目录下)
- src/main/resources/db/migration

### flyway的maven插件
```xml
            <!-- flyway 数据库迁移工具 -->
            <plugin>
                <groupId>org.flywaydb</groupId>
                <artifactId>flyway-maven-plugin</artifactId>
                <version>5.2.4</version>
                <configuration>
                    <url>jdbc:mysql://127.0.0.1:3306/dsying?useSSL=false</url>
                    <user>root</user>
                    <password>19920115asd</password>
                </configuration>
            </plugin>
```

### 执行命令
```bash
mvn flyway:migrate
```

## 单元测试 VS 集成测试
### maven脚本
```xml
            <!--    单元测试  -->
            <plugin>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.0</version>
                <configuration>
                    <!--       单元测试阶段排除 集成测试代码             -->
                    <excludes>
                        <exclude>**/*IntegrationTest</exclude>
                    </excludes>
                </configuration>
            </plugin>
            <!--   集成测试   -->
            <plugin>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>2.22.0</version>
                <configuration>
                    <!--       只包含集成测试代码             -->
                    <includes>
                        <include>**/*IntegrationTest</include>
                    </includes>
                </configuration>
            </plugin>
```
### 对应的生命周期
- 单元测试: maven的 test阶段 
```bash
mvn test
```

- 集成测试: maven的 integration阶段
```bash
mvn clear verify
```


## 持续集成
### travis ci
```yml
language: java
jdk:
  - openjdk8
script: mvn verify
```

### jenkins


## maven wrapper
**启动jenkins的docker容器中 是没有 maven的, 使用maven wrapper可以自动下载maven**

执行下面命令，生成三个文件
```bash
mvn -N io.takari:maven:wrapper
```
+ mvnw
+ mvnw.cmd
+ .mvn
    + └── wrapper
        + ├── MavenWrapperDownloader.java
        + ├── maven-wrapper.jar
        + └── maven-wrapper.properties

其中**maven-wrapper.properties**配置了 maven的下载路径
```properties
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.6.1/apache-maven-3.6.1-bin.zip
wrapperUrl=https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.5.5/maven-wrapper-0.5.5.jar
```
