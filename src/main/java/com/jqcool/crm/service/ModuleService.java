package com.jqcool.crm.service;

import com.jqcool.crm.base.BaseService;
import com.jqcool.crm.bean.Module;
import com.jqcool.crm.dto.TreeDto;
import com.jqcool.crm.mapper.ModuleMapper;
import com.jqcool.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module, Integer> {


    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有的角色信息
     *
     * @return
     */
    public List<TreeDto> findModules() {
        return moduleMapper.selectModules();
    }


    public List<TreeDto> findModulesById(Integer roleId) {

        //获取所有资源信息
        List<TreeDto> trees = moduleMapper.selectModules();

        //获取当前角色的资源信息
        List<Integer> roleModulesIds = permissionMapper.selectModulesByRoleId(roleId);

        //比对
        for (TreeDto tree : trees) {
            if (roleModulesIds.contains(tree.getId())) {
                tree.setChecked(true);
            }
        }

        return trees;
    }

    public Map<String, Object> moduleList() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Module> modules = moduleMapper.queryModules();
        result.put("count", modules.size());
        result.put("data", modules);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    /*@Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module) {
        AssertUtil.isTrue(null == module.getId() || null ==
                selectByPrimaryKey(module.getId()), "待更新记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "请指定菜单名
                称 !");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null == grade || !(grade == 0 || grade == 1 || grade == 2), "菜单层级不合
                法 !");
        Module temp
                = moduleMapper.queryModuleByGradeAndModuleName(grade, module.getModuleName());
        if (null != temp) {
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "该层级下菜单已存
                    在 !");
        }
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "请指定二级菜单url
                    值");
                    temp = moduleMapper.queryModuleByGradeAndUrl(grade, module.getUrl());
            if (null != temp) {
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "该层级下url
                        已存在 !");
            }
        }
        if (grade != 0) {
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null == parentId ||
                    null == selectByPrimaryKey(parentId), "请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "请输入权限码!");
        temp = moduleMapper.queryModuleByOptValue(module.getOptValue());
        if (null != temp) {
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())), "权限码已存在!");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(module) < 1, "菜单更新失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModule(Module module) {
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()), "请输入菜单名!");
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null == grade || !(grade == 0 || grade == 1 || grade == 2), "菜单层级不合法 !");
        AssertUtil.isTrue(null
                != moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(), module.getModuleName()), "该层级下菜单重复!");
        if (grade == 1) {
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()), "请指定二级菜单url值");
                    AssertUtil.isTrue(null!= moduleMapper.queryModuleByGradeAndUrl(module.getGrade(), module.getUrl()), "二级菜 单url不可重复 !");
        }
        if (grade != 0) {
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null == parentId ||
                    null == selectByPrimaryKey(parentId), "请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "请输入权限码!");
        AssertUtil.isTrue(null
                != moduleMapper.queryModuleByOptValue(module.getOptValue()), "权限码重复!");
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(module) < 1, "菜单添加失败!");
    }*/
}
