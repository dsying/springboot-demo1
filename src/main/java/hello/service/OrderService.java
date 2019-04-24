package hello.service;

import hello.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class OrderService {
    private UserService userService;

    @Inject
    public OrderService(UserService userService) {
        this.userService = userService;
    }

    public User placeOrder(Integer id) {
        return userService.getUserById(id);
    }
}
