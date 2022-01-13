package com.jqcool.crm.mapper;

import com.jqcool.crm.base.BaseMapper;
import com.jqcool.crm.bean.Role;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {


    //查询所有角色
    @MapKey("")
    public List<Map<String, Object>> queryAllRoles(Integer userId);

    //根据角色名查询角色
    public Role queryRoleByRoleName(String roleName);

}