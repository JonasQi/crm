package com.jqcool.crm.service;

import com.jqcool.crm.base.BaseService;
import com.jqcool.crm.bean.UserRole;
import com.jqcool.crm.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService extends BaseService<UserRole, Integer> {

    @Resource
    private UserRoleMapper userRoleMapper;
}
