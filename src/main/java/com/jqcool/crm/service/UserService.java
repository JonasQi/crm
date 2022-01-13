package com.jqcool.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jqcool.crm.base.BaseService;
import com.jqcool.crm.bean.User;
import com.jqcool.crm.bean.UserRole;
import com.jqcool.crm.mapper.UserMapper;
import com.jqcool.crm.mapper.UserRoleMapper;
import com.jqcool.crm.model.UserModel;
import com.jqcool.crm.query.UserQuery;
import com.jqcool.crm.utils.AssertUtil;
import com.jqcool.crm.utils.Md5Util;
import com.jqcool.crm.utils.PhoneUtil;
import com.jqcool.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User, Integer> {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 用户登录操作
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName, String userPwd) {

        // 1. 验证参数
        checkedUserLoginParams(userName, userPwd);

        // 2. 根据用户名，查询用户对象
        User user = userMapper.queryUserByUserName(userName);
        ;
        // 3. 判断用户是否存在 (用户对象为空，记录不存在，方法结束)
        AssertUtil.isTrue(user == null, "用户名不存在");

        //4. 用户对象不为空（用户存在，校验密码。密码不正确，方法结束）
        checkedUserPassword(userPwd, user.getUserPwd());

        //5.密码正确 (用户登录成功，返回用户的相关信息)构建返回对象
        return buildUserInfo(user);
    }

    /**
     * 构建返回的用户信息
     *
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //设置用户信息
        //将userID加密
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());

        return userModel;
    }

    /**
     * 校验用户登录参数--用户名和密码
     *
     * @param userName
     * @param userPwd
     */
    private void checkedUserLoginParams(String userName, String userPwd) {
        //用户名非空判断
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");

        //密码非空判断
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "密码不能为空");
    }


    /**
     * 校验用户登录密码
     *
     * @param userPwd
     * @param dbPassword
     */
    private void checkedUserPassword(String userPwd, String dbPassword) {

        //对输入的密码加密--因为数据库中的密码是经过加密的
        userPwd = Md5Util.encode(userPwd);
//        assert password != null;

        //判断---输入的密码和数据库加密的密码是否相同
        AssertUtil.isTrue(!userPwd.equals(dbPassword), "密码不正确");

    }


    /**
     * 修改密码
     * 1.参数校验
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     */
    public void updateUserPassword(Integer userId, String oldPassword, String newPassword, String confirmPassword) {

        //通过userId获取用户对象
        User user = userMapper.selectByPrimaryKey(userId);

        //1.参数校验
        checkPasswordParams(user, oldPassword, newPassword, confirmPassword);

        //2.设置用户密码
        user.setUserPwd(Md5Util.encode(newPassword));
        System.out.println(Md5Util.encode(oldPassword));

        //3.执行更新密码操作
        userMapper.updateByPrimaryKeySelective(user);
    }


    /**
     * 验证用户密码修改参数
     *
     * @param user
     * @param oldPassword
     * @param newPassword
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {

        //user对象 非空验证
        AssertUtil.isTrue(user == null, "用户不能为空");

        //原始密码非空验证
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "请输入原始密码");

        //原始密码 正确性验证(和数据库中对比) 注意：数据库中的密码是加密后的，所以要对输入的密码加密判断
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))), "原始密码不正确");

        //新密码非空校验
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "请输入新密码");

        //新密码和原始密码不能相同
        AssertUtil.isTrue(oldPassword.equals(newPassword), "新密码不能和原始密码相同");

        //确认密码 非空校验
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "请输入确认密码");

        //确认密码要和新密码保持一致
        AssertUtil.isTrue(!(confirmPassword.equals(newPassword)), "确认密码要和新密码保持一致");
    }


    public List<Map<String, Object>> querySales() {
        return userMapper.selectSales();
    }


    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageHelper.startPage(userQuery.getPage(), userQuery.getLimit());
        PageInfo<User> pageInfo = new PageInfo<User>(userMapper.selectByParams(userQuery));

        map.put("code", 0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }


    /**
     * 添加用户
     * 1. 参数校验
     * 用户名 非空 唯一性
     * 邮箱 非空
     * 手机号 非空 格式合法
     * 2. 设置默认参数
     * isValid 1
     * creteDate 当前时间
     * updateDate 当前时间
     * userPwd 123456 -> md5加密
     * 3. 执行添加，判断结果
     */
    /**
     * 添加用户操作
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        //1.参数校验
        checkParams(user);

        //2.设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        //3.执行添加，判断结果
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "用户添加失败!");
        System.out.println(user.getId() + "----" + user.getRoleIds());
        relationUserRole(user.getId(), user.getRoleIds());
    }

    public void relationUserRole(Integer userId, String roleIds) {

        //准备集合存储数据
        List<UserRole> list = new ArrayList<UserRole>();
        //userId roleId
        AssertUtil.isTrue(StringUtils.isBlank(roleIds), "请选择角色信息");
        //统计当前用户有多少个角色
        int count = userRoleMapper.countUserRoleNum(userId);

        if (count > 0) {
            //删除当前用户的角色
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色删除失败!");

        }
        //删除原来的角色

        String[] RoleStrId = roleIds.split(",");
        //遍历
        for (String rid : RoleStrId) {
            //准备对象
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.parseInt(rid));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());

            //存放到集合
            list.add(userRole);
        }

        //批量添加
        AssertUtil.isTrue(userRoleMapper.insertBatch(list) != list.size(), "用户角色分配失败");


    }


    /**
     * 参数校验
     *
     * @param user
     */
    private void checkParams(User user) {
        //首先用户名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空!");
        //验证用户是否存在
        User temp = userMapper.queryUserByUserName(user.getUserName());
        if (user.getId() == null) {
            AssertUtil.isTrue(temp != null, "该用户已存在!");
        } else {
            AssertUtil.isTrue(temp != null && !temp.getId().equals(user.getId()), "用户名已被使用，请重新输入！");
        }

        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "手机号码格式不正确！");

    }


    /**
     * 更新用户
     * 1. 参数校验
     * id 非空 记录必须存在
     * 用户名 非空 唯一性
     * email 非空
     * 手机号 非空 格式合法
     * 2. 设置默认参数
     * updateDate
     * 3. 执行更新，判断结果
     *
     * @param user
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        //1.参数校验
        //通过id查询用户对象
        User key = userMapper.selectByPrimaryKey(user.getId());
        System.out.println(key);
        //判断用户是否存在
        AssertUtil.isTrue(key == null, "待更新用户不存在!");
        //验证参数
        checkParams(user);
        //设置默认值
        key.setUpdateDate(new Date());
        //执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新失败!");

        relationUserRole(user.getId(), user.getRoleIds());
    }


    /**
     * (批量)删除用户
     *
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByIds(Integer[] ids) {
        //1.验证
        AssertUtil.isTrue((ids == null || ids.length == 0), "请选择要删除的用户!");
        for (Integer userId : ids) {
            //统计当前用户有多少个角色
            int count = userRoleMapper.countUserRoleNum(userId);

            if (count > 0) {
                //删除当前用户的角色
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色删除失败!");

            }
        }
        //执行删除语句
        AssertUtil.isTrue(userMapper.deleteBatch(ids) < 1, "删除失败!");
    }
}
