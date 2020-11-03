package com.itheima.service;

import com.itheima.pojo.Account;

import java.util.List;

public interface AccountService {
    List<Account> queryAll();

    void add(Account account);
}
