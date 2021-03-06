package hello.controller;

import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

/**
 * 用户模块
 * 登录，注册，登出，鉴权
 */
@RestController
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    public Result auth() {
        // 首次登录时 请求cookie中不会携带 JSESSIONID，认证失败
        // 登录成功后 再次请求，此时cookie中 就会携带 JSESSIONID，则认证成功
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication == null ? "" : authentication.getName();
        if(userName == "" || userName == "anonymousUser"){
            return new Result("false", "用户未登录", false);
        }else {
            return new Result("success", "用户已登录", true, userService.getUserByUserName(userName));
        }

    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, Object> reqBody) {
        String username = reqBody.get("username").toString();
        String password = reqBody.get("password").toString();
        if(username == null || password == null) {
            return new Result("fail", "用户名或密码不能为空", false);
        }
        if(username.length() < 1 || username.length() > 15){
            return new Result("fail", "invalid username", false);
        }
        if(password.length() < 1 || password.length() > 16){
            return new Result("fail", "invalid password", false);
        }
        try {
            // 通过给 username字段 设置 unique索引， 捕获重复异常
            userService.save(username, password);
        }catch (DuplicateKeyException e) {
            e.printStackTrace();
            return new Result("fail", "user already exist", false);
        }

        return new Result("success", "success", true);
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, Object> reqBody) {
        String username = reqBody.get("username").toString();
        String password = reqBody.get("password").toString();
        UserDetails userDetails;
        try {
            // 1 使用userDetailsService这个服务通过用户名去查找相应的 用户详情
            userDetails = userService.loadUserByUsername(username);
        }catch (UsernameNotFoundException e){
            return new Result("fail", "用户名不存在", false);
        }
        // 2 找到用户并 根据 username和password 生成token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            // 3 鉴权 token
            authenticationManager.authenticate(token);
            // 4.1 设置Response Header:   Set-Cookie: JSESSIONID=29BBFCF8EB1C92238BA9CB81B53B9023; Path=/; HttpOnly
            // 4.2 把JSESSIONID保存到上下文中
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok", "登录成功", true, new User(username));
        }catch (BadCredentialsException e){
            return new Result("fail", "密码不正确", false);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication == null ? "" : authentication.getName();
        if(userName == "" || userName == "anonymousUser"){
            return new Result("fail", "用户未登录", false);
        }else {
            // 清除上下文
            SecurityContextHolder.clearContext();
            return new Result("ok", "注销成功",true);
        }
    }


    private static class Result{
        String status;
        String msg;
        boolean isLogin;
        Object data;

        private Result(String status, String msg, boolean isLogin) {
            this(status, msg, isLogin, null);
        }

        private Result(String status, String msg, boolean isLogin, Object data) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public void setLogin(boolean login) {
            isLogin = login;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
