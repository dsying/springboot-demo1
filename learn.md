# springboot 知识点积累

## springboot静态文件处理
/static
## springboot常用注解
- @Controller
- @RestController
- @RequestMapping
- @GetMapping
- @PostMapping
- @RequestBody
- @ResponseBody
- @RequestParam

## 注册Spring Security - 密码编码
**不以明文形式存储密码** [参考地址](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)
### 定义密码编码器
我们首先将简单的BCryptPasswordEncoder定义为我们配置中的bean：
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```