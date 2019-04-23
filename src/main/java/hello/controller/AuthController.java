package hello.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    @PostMapping("auth/login")
    public void login(@RequestBody Map<String, Object> reqBody) {
        System.out.println(reqBody);
    }
}
