package hello.controller;

import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

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
}