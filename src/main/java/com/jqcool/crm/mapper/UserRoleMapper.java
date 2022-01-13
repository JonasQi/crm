package com.jqcool.crm.mapper;

import com.jqcool.crm.base.BaseMapper;
import com.jqcool.crm.bean.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole, Integer> {

    int countUserRoleNum(Integer userId);

    int deleteUserRoleByUserId(Integer userId);
}