package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import com.liu.crm.query.SaleChanceQuery;
import com.liu.crm.service.SaleChanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;

    /**
     * 营销机会数据 多条件 分页 查询
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会管理页面
     * @return
     */
    @RequestMapping("/index")
    public  String index(){
        return "saleChance/sale_chance";
    }
}
