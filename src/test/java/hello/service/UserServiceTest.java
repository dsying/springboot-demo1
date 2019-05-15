package hello.service;

import hello.entity.User;
import hello.dao.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave(){
        // 调用userService
        // 验证userService将请求转发给了userMapper

        // given: 给定一个条件(如果passwordEncoder的encode方法被调用，返回一个值)
        when(passwordEncoder.encode("myPassword")).thenReturn("myEncodedPassword");

        // when: 当条件被执行时
        userService.save("myUser", "myPassword");

        // then: 验证操作是否正确
        verify(userMapper).save("myUser", "myEncodedPassword");
    }

    @Test
    public void testGetUserByUserName(){
        // 当 userService调用getUserByUserName方法时
        userService.getUserByUserName("myUser");
        // 验证 userMapper的findUserByUsername 方法是否被调用
        verify(userMapper).findUserByUsername("myUser");
    }

    @Test
    public void throwExceptionWhenUserNotFound(){
        // 断言:  我敢断言 某个操作一定会抛出UsernameNotFoundException异常
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("myUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound(){
        when(userMapper.findUserByUsername("myUser"))
                .thenReturn(new User(123, "myUser", "myEncodedPassword"));

        UserDetails userDetails = userService.loadUserByUsername("myUser");

        Assertions.assertEquals("myUser", userDetails.getUsername());
    }
}
