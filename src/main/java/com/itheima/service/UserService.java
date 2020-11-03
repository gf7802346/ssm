package com.itheima.service;

import com.itheima.pojo.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User login(User loginUser);
}
