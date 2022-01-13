package com.jqcool.crm.controller;

import com.jqcool.crm.base.BaseController;
import com.jqcool.crm.bean.User;
import com.jqcool.crm.service.PermissionService;
import com.jqcool.crm.service.UserService;
import com.jqcool.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {

        return "index";
    }

    /**
     * 后台资源页面
     *
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest req) {
        //通过工具类,从cookie 中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);

        //调用对应Service层的方法，通过userId主键查询用户对象
        User user = userService.selectByPrimaryKey(userId);

        //将用户对象设置到request作用域中
        req.setAttribute("user", user);


        //将用户的权限吗存储到Session
        List<String> permissionIds = permissionService.queryUserHasRolesHasPermissions(userId);
        //将用户的权限存到Session作用域
        req.getSession().setAttribute("permissions", permissionIds);
        return "main";
    }

    /**
     * 欢迎页面
     *
     * @return
     */
    @RequestMapping("welcome")
    public String welcome() {

        return "welcome";
    }


}
