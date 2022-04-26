package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import com.liu.crm.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserRoleController extends BaseController {

    @Autowired
    private UserRoleService userRoleService;
}
