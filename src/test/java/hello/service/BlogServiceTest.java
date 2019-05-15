package hello.service;

import hello.dao.BlogDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * TDD 测试驱动开发 的核心就是 先写一个必定会失败的测试，然后逐步修改bug
 */
@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogDao blogDao;
    @InjectMocks
    BlogService blogService;

    @Test
    void getBlogsFromDb() {
        // when
        blogService.getBlogs(1, 10, null);
        // then
        Mockito.verify(blogDao).getBlogs(1, 10, null);
    }
}
