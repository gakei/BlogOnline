package com.github.hcsp.mapper;

import com.github.hcsp.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE ID = #{ID}")
    User findUserById(@Param("ID") Integer id);

    @Select("SELECT * FROM user WHERE USERNAME = #{USERNAME}")
    User getUserByUsername(@Param("USERNAME") String username);

    @Insert("insert into user(username, encrypted_password, created_at, updated_at)" +
            "values(#{username}, #{encryptedPassword}, now(), now())")
    void save(@Param("username") String username, @Param("encryptedPassword") String encryptedPassword);
}
