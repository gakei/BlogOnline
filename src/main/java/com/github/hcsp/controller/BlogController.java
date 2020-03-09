package com.github.hcsp.controller;

import com.github.hcsp.dao.BlogDao;
import com.github.hcsp.entity.BlogListResult;
import com.github.hcsp.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
public class BlogController {
    private final BlogDao blogDao;
    private final BlogService blogService;

    @Inject
    public BlogController(BlogDao blogDao, BlogService blogService) {
        this.blogDao = blogDao;
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public BlogListResult getBlogs(@RequestParam("page")Integer page,
                                   @RequestParam(value = "userId", required = false)Integer userId) {
        if (page == null || page < 0) {
            page = 1;
        }
        return blogService.getBlogs(page, 10, userId);
    }
}
