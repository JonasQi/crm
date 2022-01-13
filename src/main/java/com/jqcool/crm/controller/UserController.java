package com.jqcool.crm.controller;

import com.jqcool.crm.base.BaseController;
import com.jqcool.crm.base.ResultInfo;
import com.jqcool.crm.bean.User;
import com.jqcool.crm.model.UserModel;
import com.jqcool.crm.query.UserQuery;
import com.jqcool.crm.service.UserService;
import com.jqcool.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 执行登录操作
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {

        System.out.println(userName);
        System.out.println(userPwd);
        ResultInfo resultInfo = new ResultInfo();

        //调用Service层的登录方法，得到返回的用户对象
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);
        /**
         * 登录成功后，有两种处理：
         * 1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端
         需要重复登录 ）
         * 2. 将用户信息返回给客户端，由客户端（Cookie）保存
         */
        return resultInfo;
    }


    /**
     * 执行修改密码操作
     *
     * @param req
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest req, String oldPassword, String newPassword, String confirmPassword) {

        ResultInfo resultInfo = new ResultInfo();

        //获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);

        //调用Service层修改密码的方法
        userService.updateUserPassword(userId, oldPassword, newPassword, confirmPassword);

        return resultInfo;
    }


    /**
     * 跳转到修改密码界面
     *
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    /**
     * 跳转到用户管理页面
     *
     * @return
     */
    @RequestMapping("index")
    public String toUserControlPage() {
        return "user/user";
    }

    /**
     * 跳转到修改信息界面
     *
     * @param req
     * @return
     */
    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req) {

        //获取用户Id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user", user);
        //转发
        return "user/setting";
    }


    /**
     * 执行修改基本信息操作
     *
     * @param user
     * @return
     */
    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo updateUserPage(User user) {

        ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.updateByPrimaryKeySelective(user);

        //返回目标数据对象
        return resultInfo;
    }


    /**
     * 添加用户操作
     *
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功!");
    }

    /**
     * 更新或添加操作
     *
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功!");
    }


    /**
     * 进入用户添加或更新页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model) {
        if (null != id) {
            model.addAttribute("user", userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }


    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String, Object>> findSales() {

        return userService.querySales();
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {

        return userService.queryUserByParams(userQuery);
    }


    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] ids) {

        userService.deleteUserByIds(ids);

        return success("删除成功!");
    }

}
