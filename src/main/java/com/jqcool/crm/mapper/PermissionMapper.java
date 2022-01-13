package com.jqcool.crm.mapper;

import com.jqcool.crm.base.BaseMapper;
import com.jqcool.crm.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission, Integer> {

    int countPermissionByRoleId(Integer roleId);

    int deletePermissionsByRoleId(Integer roleId);

    List<Integer> selectModulesByRoleId(Integer roleId);

    List<String> queryUserHasRolesHasPermissions(Integer userId);
}