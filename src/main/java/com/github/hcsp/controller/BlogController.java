package com.github.hcsp.controller;

import com.github.hcsp.entity.Blog;
import com.github.hcsp.entity.BlogListResult;
import com.github.hcsp.entity.BlogResult;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.AuthService;
import com.github.hcsp.service.BlogService;
import com.github.hcsp.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

import static com.sun.xml.internal.ws.api.message.Packet.Status.Response;

@Controller
public class BlogController {
    private final AuthService authService;
    private final BlogService blogService;

    @Inject
    public BlogController(AuthService authService, BlogService blogService) {
        this.authService = authService;
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public BlogListResult getBlogs(@RequestParam("page")Integer page,
                                   @RequestParam(value = "userId", required = false)Integer userId,
                                   @RequestParam(value = "atIndex", required = false) boolean atIndex) {
        if (page == null || page < 0) {
            page = 1;
        }
        return blogService.getBlogs(page, 10, userId, atIndex);
    }

    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult getBlog(@PathVariable("blogId")int blogId) {
        return blogService.getBlogById(blogId);
    }

    @PostMapping("/blog")
    @ResponseBody
    public BlogResult newBlog(@RequestBody Map<String, Object> param) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.insertBlog(fromParam(param, user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e);
        }
    }

    @PatchMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult updateBlog(@PathVariable("blogId")int blogId
            , @RequestBody Map<String, Object> param) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlog(blogId, fromParam(param, user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.failure(e);
        }
    }

    @DeleteMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult deleteBlog(@PathVariable("blogId") int blogId) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.deleteBlog(blogId, user))
                    .orElse(BlogResult.failure("登陆后才能操作"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return BlogResult.failure(e);
        }
    }

    private Blog fromParam(Map<String, Object> params, User user) {
        Blog blog = new Blog();
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        String description = (String) params.get("description");
        boolean atIndex = (boolean) params.get("atIndex");

        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "title is invalid");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 10000, "content is invalid");

        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }

        blog.setTitle(title);
        blog.setContent(content);
        blog.setDescription(description);
        blog.setUser(user);
        blog.setAtIndex(atIndex);
        return blog;
    }
}
