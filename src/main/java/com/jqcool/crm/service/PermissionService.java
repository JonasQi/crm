package com.jqcool.crm.service;

import com.jqcool.crm.base.BaseService;
import com.jqcool.crm.bean.Permission;
import com.jqcool.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission, Integer> {


    @Resource
    private PermissionMapper permissionMapper;


    public List<String> queryUserHasRolesHasPermissions(Integer userId) {
        return permissionMapper.queryUserHasRolesHasPermissions(userId);
    }
}
