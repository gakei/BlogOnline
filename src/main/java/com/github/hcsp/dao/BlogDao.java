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

    private Map<String, Object> asMap(Object... arges) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < arges.length; i += 2) {
            result.put(arges[i].toString(), arges[i + 1]);
        }
        return result;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        Map<String, Object> parameters = asMap("userId", userId,
                "offset", (page - 1) * pageSize,
                "limit", pageSize);
        return sqlSession.selectList("selectBlog", parameters);
    }

    public int count(Integer userId) {
        return sqlSession.selectOne("countBlog", asMap("userId", userId));
    }
}
