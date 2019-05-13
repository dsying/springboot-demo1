package hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    UserService userService;
    @Mock
    AuthenticationManager authenticationManager;

    //加密解密器
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService, authenticationManager)).build();
    }

    /**
     * Junit测试用例是如何执行的?
     *
     *     AuthControllerTest testInstance1 = new AuthControllerTest();
     *     testInstance1.setUp(); // 每个测试用例执行前 都会执行 @BeforeEach 声明的方法
     *     testInstance1.test1();o
     *
     *     AuthControllerTest testInstance2 = new AuthControllerTest();
     *     testInstance2.setUp();
     *     testInstance2.test2();
     *
     *     Junit在执行每个测试用例之前都会 新创建一个实例对象，通过这个实例对象去执行该测试用例
     *
     *     并不是 用一个实例对象 去执行 所有的 测试用例
     *
     *     目的是 避免测试用例间 状态或者变量 共享
     */

    @Test
    void returnNotLoginByDefault() throws Exception{
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    System.out.println(mvcResult.getResponse().getContentAsString());
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户未登录"));
                });
    }

    @Test
    void testLogin() throws Exception{
        // 未登录时， /auth 校验返回 未登录
        mockMvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    System.out.println(mvcResult.getResponse().getContentAsString());
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户未登录"));
                });

        // 登录
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "MyUser");
        userInfo.put("password", "MyPassword");

        String requestBody = new ObjectMapper().writeValueAsString(userInfo);
            // 因为 userService是 MOCK的 当 调用loadUserByUsername时 我们要返回一个真正的 UserDetails
        when(userService.loadUserByUsername("MyUser"))
                .thenReturn(new User("MyUser", bCryptPasswordEncoder.encode("MyPassword"), Collections.emptyList()));

            // 登录是一个post接口，传递一个json
        MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("登录成功"));
                    }
                })
                .andReturn();

        HttpSession session = mvcResult.getRequest().getSession();


        when(userService.getUserByUserName("MyUser"))
                .thenReturn(new hello.entity.User(123, "MyUser", bCryptPasswordEncoder.encode("MyPassword")));

        // 登录之后 再 查看是否登录
        mockMvc.perform(get("/auth").session((MockHttpSession)session)).andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("MyUser"));
                });

        mockMvc.perform(get("/auth/logout")).andExpect(status().isOk())
                .andExpect(logoutResult -> {
                    System.out.println(logoutResult.getResponse().getContentAsString());
                    Assertions.assertTrue(logoutResult.getResponse().getContentAsString().contains("注销成功"));
                });
    }

    @Test
    void testLogout() throws Exception{
        mockMvc.perform(get("/auth/logout")).andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    System.out.println(mvcResult.getResponse().getContentAsString());
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户未登录"));
                });
    }
}