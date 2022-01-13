package com.jqcool.crm.mapper;

import com.jqcool.crm.base.BaseMapper;
import com.jqcool.crm.bean.User;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User, Integer> {

    User queryUserByUserName(String userName);

    List<Map<String, Object>> selectSales();
    
}