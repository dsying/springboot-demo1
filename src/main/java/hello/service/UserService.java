package hello.service;

import hello.entity.User;
import hello.dao.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    @Inject
    public UserService(PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
//        save("dsying", "19920115asd");
    }

    // 存储 加密后的密码
    public void save(String username, String password) {
        userMapper.save(username, passwordEncoder.encode(password));
    }
    // 所谓接口就是一个功能的约定，我不关心你是如何实现的，我只关心你通过这个接口能告诉我什么信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 加密后的密码
        String encodedPassword = user.getEncryptedPassword();
        // 该User是 UserDetails接口的一个实现
        return new org.springframework.security.core.userdetails.User(username, encodedPassword, Collections.emptyList());
    }

    public User getUserByUserName(String username){
        return userMapper.findUserByUsername(username);
    }
}
