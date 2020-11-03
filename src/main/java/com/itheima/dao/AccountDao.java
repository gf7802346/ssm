package com.itheima.dao;

import com.itheima.pojo.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AccountDao {
    @Select("select * from account")
    List<Account> queryAll();

    @Insert("insert into account (name,money) values (#{name},#{money})")
    void add(Account account);
}
