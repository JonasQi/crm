package com.jqcool.crm.interceptors;


import com.jqcool.crm.exceptions.NoLoginException;
import com.jqcool.crm.service.UserService;
import com.jqcool.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //未登录,拦截
        // 获取Cookie中的用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断用户ID是否不为空，且数据库中存在对应的用户记录
        if (null == userId || null == userService.selectByPrimaryKey(userId)) {
            // 抛出未登录异常
            throw new NoLoginException("请先进行登录");
        }

        //放行
        return true;
    }
}
