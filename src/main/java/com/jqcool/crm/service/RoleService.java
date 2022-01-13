package com.jqcool.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jqcool.crm.base.BaseService;
import com.jqcool.crm.bean.Permission;
import com.jqcool.crm.bean.Role;
import com.jqcool.crm.mapper.ModuleMapper;
import com.jqcool.crm.mapper.PermissionMapper;
import com.jqcool.crm.mapper.RoleMapper;
import com.jqcool.crm.query.RoleQuery;
import com.jqcool.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有角色
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {

        return roleMapper.queryAllRoles(userId);
    }


    /**
     * 分页查询
     *
     * @param roleQuery
     * @return
     */
    public Map<String, Object> queryByParamsForTable(RoleQuery roleQuery) {
        //实例化
        Map<String, Object> map = new HashMap<String, Object>();
        //开启分页
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getLimit());
        PageInfo<Role> list = new PageInfo<>(selectByParams(roleQuery));
        //准备数据
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", list.getTotal());
        map.put("data", list.getList());

        //返回数据
        return map;
    }


    /**
     * 角色信息添加
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role) {
        //角色名非空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "请输入角色名!");
        //根据角色名查询角色信息
        Role roleMsg = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(roleMsg != null, "该角色已存在!");

        //设置默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());

        //执行添加操作
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色记录添加失败!");
    }


    /**
     * 角色信息更新
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {

        AssertUtil.isTrue(null == role.getId() || null == selectByPrimaryKey(role.getId()), "待修改的记录不存在!");

        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "请输入角色名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(role.getId())), "该角色已存在!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role) < 1, "角色记录更新失败!");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        Role temp = selectByPrimaryKey(roleId);
       /* System.out.println(roleId);
        System.out.println(temp);*/
        AssertUtil.isTrue(null == roleId || null == temp, "待删除的记录不存在!");
        temp.setIsValid(0);
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(temp) < 1, "角色记录删除失败!");
    }


    /**
     * 角色授权
     *
     * @param mids
     * @param roleId
     */
   /* @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer[] mids, Integer roleId) {
        */

    /**
     * 核心表-t_permission t_role(校验角色存在)
     * 如果角色存在原始权限 删除角色原始权限
     * 然后添加角色新的权限 批量添加权限记录到t_permission
     *//*
        Role temp = selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null == roleId || null == temp, "待授权的角色不存在!");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0) {
            AssertUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId) != count, "角色权限分配失败!");
        }

        if (null != mids && mids.length > 0) {
            List<Permission> permissions = new ArrayList<Permission>();
            for (Integer mid : mids) {
                Permission permission = new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.insertBatch(permissions) != permissions.size(), "授权失败");
//            permissionMapper.insertBatch(permissions);
        }
    }*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer[] mids, Integer roleId) {
        /**
         * 核心表-t_permission t_role(校验角色存在)
         * 如果角色存在原始权限 删除角色原始权限
         * 然后添加角色新的权限 批量添加权限记录到t_permission
         */
        Role temp = selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null == roleId || null == temp, "待授权的角色不存在!");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if (count > 0) {
            AssertUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId)
                    < count, "权限分配失败!");
        }
        if (null != mids && mids.length > 0) {
            List<Permission> permissions = new ArrayList<Permission>();
            for (Integer mid : mids) {
                Permission permission = new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
            permissionMapper.insertBatch(permissions);
        }
    }
}
