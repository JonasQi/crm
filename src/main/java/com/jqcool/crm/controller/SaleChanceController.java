package com.jqcool.crm.controller;

import com.jqcool.crm.base.BaseController;
import com.jqcool.crm.base.ResultInfo;
import com.jqcool.crm.bean.SaleChance;
import com.jqcool.crm.query.SaleChanceQuery;
import com.jqcool.crm.service.SaleChanceService;
import com.jqcool.crm.service.UserService;
import com.jqcool.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;

    /**
     * 多条件分页查询营销机会
     *
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery query) {
        return saleChanceService.querySaleChanceByParams(query);
    }

    /**
     * 进入营销机会页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }


    /**
     * 机会数据添加与更新页面视图转发
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model) {
        // 如果id不为空，表示是修改操作，修改操作需要查询被修改的数据
        if (null != id) {
            // 通过主键查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            // 将数据存到作用域中
            model.addAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }


    /**
     * 机会参数添加
     *
     * @param req
     * @param saleChance
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req, SaleChance saleChance) {

        //获取登录用户的id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();

        //创建人
        saleChance.setCreateMan(trueName);

        //添加成功
        saleChanceService.saveSaleChance(saleChance);
        return success("添加成功");
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(HttpServletRequest req, SaleChance saleChance) {

        //更新操作
        saleChanceService.updateSaleChance(saleChance);
        //返回目标对象
        return success("更新成功");
    }


    /**
     * (批量)删除营销机会数据
     *
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids) {
        // 删除营销机会的数据
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

}
