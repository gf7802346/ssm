package com.itheima;

import com.itheima.dao.AccountDao;
import com.itheima.pojo.Account;
import com.itheima.service.AccountService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SsmTest {
    @Test
    public void testService() {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring-service.xml");
        AccountService accountService = app.getBean(AccountService.class);
        accountService.queryAll();
    }

    @Test
    public void testAccount() throws IOException {
        InputStream is = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSession session = new SqlSessionFactoryBuilder().build(is).openSession();
        AccountDao accountDao = session.getMapper(AccountDao.class);
        List<Account> accountList = accountDao.queryAll();
        for (Account account : accountList) {
            System.out.println(account);
        }
        session.close();
        is.close();
    }

    @Test
    public void testSpringDao() {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring-dao.xml");
        AccountDao accountDao = app.getBean(AccountDao.class);
        List<Account> accountList = accountDao.queryAll();
        for (Account account : accountList) {
            System.out.println(account);
        }
    }

}
