package com.jqcool.crm.controller;

import com.jqcool.crm.base.BaseController;
import com.jqcool.crm.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("permission")
public class PermissionController extends BaseController {

    @Resource
    private PermissionService permissionService;

}
