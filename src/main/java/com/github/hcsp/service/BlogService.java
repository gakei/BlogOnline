package com.github.hcsp.service;

import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogListResult;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {
    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao, UserService userService) {
        this.blogDao = blogDao;
    }

    public BlogListResult getBlogs(Integer page, Integer pageSize, Integer userId, boolean atIndex) {
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId, atIndex);

            int count = blogDao.count(userId);

            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;

            return BlogListResult.success(blogs, count, page, pageCount);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogListResult.failure("系统异常");
        }
    }

    public BlogResult getBlogById(int blogId) {
        try {
            return BlogResult.success("获取成功", blogDao.selectBlogById(blogId));
        } catch (Exception e) {
            return BlogResult.failure(e);
        }
    }

    public BlogResult insertBlog(Blog newBlog) {
        try {
            return BlogResult.success("创建成功", blogDao.insertBlog(newBlog));
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure(e);
        }
    }

    public BlogResult updateBlog(int blogId, Blog targetBlog) {
        Blog blogInDb = blogDao.selectBlogById(blogId);
        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }

        if (!Objects.equals(blogId, blogInDb.getId())){
            return BlogResult.failure("无法修改别人的博客");
        }

        try {
            targetBlog.setId(blogId);
            return BlogResult.success("修改成功", blogDao.updateBlog(targetBlog));
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure(e);
        }
    }

    public BlogResult deleteBlog(int blogId, User user) {
        Blog blogInDb = blogDao.selectBlogById(blogId);
        if (blogInDb == null) {
            return BlogResult.failure("博客不存在");
        }

        if (!Objects.equals(user.getId(), blogInDb.getUserId())){
            return BlogResult.failure("无法修改别人的博客");
        }

        try {
            blogDao.deleteBlog(blogId);
            return BlogResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.failure(e);
        }
    }
}
