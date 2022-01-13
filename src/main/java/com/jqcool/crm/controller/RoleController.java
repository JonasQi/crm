package com.jqcool.crm.controller;

import com.jqcool.crm.annotation.RequiredPermission;
import com.jqcool.crm.base.BaseController;
import com.jqcool.crm.base.ResultInfo;
import com.jqcool.crm.bean.Role;
import com.jqcool.crm.query.RoleQuery;
import com.jqcool.crm.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {


    @Resource
    private RoleService roleService;


    /**
     * 查询所有角色数据
     *
     * @param userId
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId) {

        return roleService.queryAllRoles(userId);
    }


    /**
     * 跳转到角色管理页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {

        return "role/role";
    }

    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, Model model) {

        model.addAttribute("roleId", roleId);
        return "role/grant";
    }


    /**
     * 角色列表展示
     *
     * @param roleQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code = "60")
    public Map<String, Object> roleList(RoleQuery roleQuery) {

        return roleService.queryByParamsForTable(roleQuery);
    }


    //跳转到添加或更新页面
    @RequestMapping("addOrUpdateRolePage")
    public String addRolePage(Integer id, Model model) {

        if (null != id) {
            model.addAttribute("role", roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }


    //添加角色
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role) {
        roleService.saveRole(role);
        return success("添加成功!");
    }

    //更新角色
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role) {
        roleService.updateRole(role);
        return success("角色记录更新成功");
    }


    //删除角色
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId) {
//        System.out.println(roleId);
        roleService.deleteRole(roleId);
        return success("删除成功");
    }

    /**
     * 角色授权
     *
     * @param mids
     * @param roleId
     * @return
     */
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids, Integer roleId) {
        roleService.addGrant(mids, roleId);
        return success("权限添加成功");
    }
}
