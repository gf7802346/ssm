package com.itheima.dao;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {
    @Select("select * from user where username=#{username} and password=#{password}")
    User login(User loginUser);
}
