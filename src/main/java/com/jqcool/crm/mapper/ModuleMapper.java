package com.jqcool.crm.mapper;

import com.jqcool.crm.base.BaseMapper;
import com.jqcool.crm.bean.Module;
import com.jqcool.crm.dto.TreeDto;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, Integer> {

    public List<TreeDto> selectModules();

    List<Module> queryModules();
    
}