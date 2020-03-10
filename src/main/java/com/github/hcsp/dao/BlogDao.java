package com.github.hcsp.dao;

import com.github.hcsp.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId, boolean atIndex) {
        Map<String, Object> parameters = asMap("userId", userId,
                "offset", (page - 1) * pageSize,
                "limit", pageSize,
                "atIndex", atIndex);
        return sqlSession.selectList("selectBlog", parameters);
    }

    public int count(Integer userId) {
        return sqlSession.selectOne("countBlog", asMap("userId", userId));
    }

    public Blog selectBlogById(int blogId) {
        return sqlSession.selectOne("selectBlogById", asMap("id", blogId));
    }

    private Map<String, Object> asMap(Object... arges) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < arges.length; i += 2) {
            result.put(arges[i].toString(), arges[i + 1]);
        }
        return result;
    }

    public Blog insertBlog(Blog newBlog) {
        sqlSession.insert("insertBlog", newBlog);
        return selectBlogById(newBlog.getId());
    }

    public Blog updateBlog(Blog targetBlog) {
        sqlSession.update("updateBlog", targetBlog);
        return selectBlogById(targetBlog.getId());
    }

    public void deleteBlog(int blogId) {
        sqlSession.delete("deleteBlog", blogId);
    }
}
