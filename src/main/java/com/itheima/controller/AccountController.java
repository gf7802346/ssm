package com.itheima.controller;

import com.itheima.pojo.Account;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("accountController")
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/queryAll")
    public String queryAll(Model model) {
        System.out.println("queryAll()方法执行了");
        List<Account> accounts= accountService.queryAll();
        model.addAttribute("accounts", accounts);
        return "list";
    }

    @RequestMapping("/add")
    public String add(Account account) {
        System.out.println("add()方法执行了");
        System.out.println(account);
        accountService.add(account);
        return "redirect:/account/queryAll";
    }
}
