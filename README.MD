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
```bash
docker run --name mysql1 -e MYSQL_ROOT_PASSWORD=my-secret-pw -e MYSQL_DATABASE=xdml -p 3306:3306 -d mysql
```
docker run --name mysql1 mysql: 使用mysql这个镜像创建一个名为 mysql1 的 容器 
-e MYSQL_ROOT_PASSWORD=my-secret-pw： 指定root用户密码
-e MYSQL_DATABASE=xdml 指定数据库名
-p 3306:3306 将宿主(本地)端口 和 容器端口 绑定