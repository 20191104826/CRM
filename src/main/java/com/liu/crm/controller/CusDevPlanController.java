package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    /**
     * 进入客户开发计划页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }
}
