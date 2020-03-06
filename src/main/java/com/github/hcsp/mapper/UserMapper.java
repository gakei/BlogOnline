package com.github.hcsp.mapper;

import com.github.hcsp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE ID = #{ID}")
    User findUserById(@Param("ID") Integer id);

}
