package com.itheima.interceptors;

import com.itheima.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PriviligesInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            System.out.println("未登录，即将跳转到登录页面");
            response.sendRedirect(request.getContextPath() + "/pages/login.jsp");
            return false;
        } else {
            return true;
        }
    }
}
