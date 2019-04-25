package hello.controller;

import hello.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class AuthController {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    public Result auth(){
        return new Result("success", "成功", true);
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, Object> reqBody) {
        String username = reqBody.get("username").toString();
        String password = reqBody.get("password").toString();
        UserDetails userDetails;
        try {
            // 1 使用userDetailsService这个服务通过用户名去查找相应的 用户详情
            userDetails = userDetailsService.loadUserByUsername(username);
        }catch (UsernameNotFoundException e){
            return new Result("fail", "用户名不存在", false);
        }
        // 2 找到用户并 根据 username和password 生成token
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password);
        try {
            // 3 鉴权 token
            authenticationManager.authenticate(token);
            // 4 保存 token 到上下文中
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("OK", "登录成功", true, new User(1, "张三"));
        }catch (BadCredentialsException e){
            return new Result("fail", "密码不正确", false);
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
