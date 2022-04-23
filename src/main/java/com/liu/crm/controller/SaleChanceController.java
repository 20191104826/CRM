package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import com.liu.crm.base.ResultInfo;
import com.liu.crm.enums.StateStatus;
import com.liu.crm.query.SaleChanceQuery;
import com.liu.crm.service.SaleChanceService;
import com.liu.crm.utils.CookieUtil;
import com.liu.crm.utils.LoginUserUtil;
import com.liu.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;

    /**
     * 营销机会数据 多条件 分页 查询
     *      如果flag的值不为空，且值为1，则表示当前查询的是客户开发计划；否则查询营销机会数据
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,
                                                      Integer flag,HttpServletRequest request){
        //判断flag的值
        if(flag != null && flag ==1){
            //查询客户开发计划
            //设置分配状态
            saleChanceQuery.setIsState(StateStatus.STATED.getType());
            //设置指派人  （当前登录用户的ID）
            //从cookie中获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }

        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }




    /**
     * 进入营销机会管理页面
     * @return
     */
    @RequestMapping("index")
    public  String index(){
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会
     *
     * @param saleChance
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        //createMan创建人
        //从Cookie 中获取当前登录的用户名
        String userName = CookieUtil.getCookieValue(request,"userName");
        //设置用户名到营销机会对象
        saleChance.setCreateMan(userName);
        //调用Service层的添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功！");
    }

    /**
     * 更新营销机会
     *
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        //调用Service层的添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    /**
     * 进入添加/修改营销机会数据页面
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceID,HttpServletRequest request){

        //判断营销机会ID是否为空
        if(saleChanceID != null){
            //通过ID查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceID);
            //将数据设置到请求域中
            request.setAttribute("saleChance",saleChance);
        }

        return "saleChance/add_update";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        //调用Service层的删除方法
        saleChanceService.deleteSaleChance(ids);
        return success("营销机会数据删除成功！");
    }
}
