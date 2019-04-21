package hello;

import hello.service.OrderService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@RestController
public class HelloController {
    private OrderService orderService;

    /*
    *  使用构造器注入
    * Always use constructor based dependency injection in your beans. Always use assertions for mandatory dependencies
    * */
    @Inject
    public HelloController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}