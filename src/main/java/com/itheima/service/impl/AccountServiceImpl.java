package com.itheima.service.impl;

import com.itheima.dao.AccountDao;
import com.itheima.pojo.Account;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public List<Account> queryAll() {
        System.out.println("AccountServiceImpl.queryAll()");
        return accountDao.queryAll();
    }

    @Override
    public void add(Account account) {
        System.out.println("AccountServiceImpl.add()");
        accountDao.add(account);
    }
}
