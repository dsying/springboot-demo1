package hello.dao;

import hello.entity.Blog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BlogDao {
    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId){
        System.out.println("blogdao.getBlogs");
        return null;
    }
}
