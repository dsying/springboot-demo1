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