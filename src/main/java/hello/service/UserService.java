package hello.service;

import hello.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {
    private PasswordEncoder passwordEncoder;

    private Map<String, String> userMap = new ConcurrentHashMap<>();

    @Inject
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.save("dsying", "dsying");
    }

    // 存储 加密后的密码
    public void save(String username, String password) {
        userMap.put(username, passwordEncoder.encode(password));
    }
    // 获取密码
    public String getPassword(String username) {
        return userMap.get(username);
    }

    // 所谓接口就是一个功能的约定，我不关心你是如何实现的，我只关心你通过这个接口能告诉我什么信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!userMap.containsKey(username)){
            throw new UsernameNotFoundException("用户不存在");
        }
        // 加密后的密码
        String encodedPassword = userMap.get(username);
        // 该User是 UserDetails接口的一个实现
        return new org.springframework.security.core.userdetails.User(username, encodedPassword, Collections.emptyList());
    }

    public User getUserByUserName(String username){
        return new User(1, "dsying");
    }
}
