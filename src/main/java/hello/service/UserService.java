package hello.service;

public class UserService {
    public User getUserById(Integer id, String name) {
        return new User(id, name);
    }
}
