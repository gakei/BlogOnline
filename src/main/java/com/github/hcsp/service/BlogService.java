package com.github.hcsp.service;

import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogListResult;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService) {
        this.blogDao = blogDao;
    }

    public BlogListResult getBlogs(Integer page, Integer pageSize, Integer userId) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, page, userId);

            int count = blogDao.count(userId);

            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;

            return BlogListResult.success(blogs, count, page, pageCount);
        } catch (Exception e) {
            return BlogListResult.failure("系统异常");
        }
    }
}
