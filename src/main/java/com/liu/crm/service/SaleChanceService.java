package com.liu.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.liu.crm.base.BaseService;
import com.liu.crm.dao.SaleChanceMapper;
import com.liu.crm.enums.DevResult;
import com.liu.crm.enums.StateStatus;
import com.liu.crm.query.SaleChanceQuery;
import com.liu.crm.utils.AssertUtil;
import com.liu.crm.utils.PhoneUtil;
import com.liu.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会
     *  返回的数据格式必须满足 Layui 中数据表格要求的格式
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery){

        Map<String,Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //得到对应的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        //设置 map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());

        //设置分页好的列表
        map.put("data",pageInfo.getList());

        return map;
    }

    /**
     * 添加营销机会
     *  1.参数校验
     *      customerName 客户名称，   非空
     *      linkMan     联系人       非空
     *      linkPhone   联系号码      非空
     *  2.设置相关参数的默认值
     *      createMan创建人         当前登录用户名
     *      assignMan指派人
     *          如果未设置指派人（默认）
     *              is_state 分配状态（0=未分配，  1=已分配）
     *                  0=未分配
     *              assignTime 指派时间
     *                  设置为 null
     *              devResult开发状态（0=未开发， 1=开发中， 2=开发成功， 3=开发失败）
     *                  0=未开发（默认）
     *          如果设置了指派人
     *              is_state 分配状态（0=未分配，  1=已分配）
     *                 1=已分配
     *              assignTime 指派时间
     *      *          系统当前时间
     *              devResult开发状态（0=未开发， 1=开发中， 2=开发成功， 3=开发失败）
     *                 1=开发中
     *      isValid 是否有效  (0=无效，1=有效)
     *         设置为1=有效
     *      createDate创建时间
     *          默认是系统当前时间
     *      updateDate
     *          默认是系统当前时间
     *  3.执行添加操作，判断受影响行数
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        /* 1.参数校验 */
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        /*设置相关字段的默认值*/
        // isValid 是否有效  (0=无效，1=有效)
        saleChance.setIsValid(1);
        //createDate创建时间  默认是系统当前时间
        saleChance.setCreateDate(new Date());
        //updateDate 默认是系统当前时间
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            //如果为空，则表示未设置指派人
            // is_state 分配状态（0=未分配，  1=已分配） 0=未分配（默认）
            saleChance.setIsState(StateStatus.UNSTATE.getType());
            //assignTime 指派时间  设置为 null 默认
            saleChance.setAssignTime(null);
            //devResult开发状态（0=未开发， 1=开发中， 2=开发成功， 3=开发失败） 0=未开发（默认）
            saleChance.setDevResult(DevResult.UNDEV.getStatus());

        }else{
            //如果不为空，则表示设置了指派人
            //is_state 分配状态（0=未分配，  1=已分配） 1=已分配  （默认）
            saleChance.setIsState(StateStatus.STATED.getType());
            //assignTime 指派时间   系统当前时间
            saleChance.setAssignTime(new Date());
            //devResult开发状态（0=未开发， 1=开发中， 2=开发成功， 3=开发失败） 1=开发中（默认）
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //3.执行添加操作，判断受影响行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1,"添加营销机会失败！");

    }

    /**
     * 1.参数校验
     *    customerName 客户名称，   非空
     *    linkMan     联系人       非空
     *    linkPhone   联系号码      非空
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        //customerName 客户名称，   非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"用户名不能为空！");
        //linkMan     联系人       非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空！");
        //linkPhone   联系号码      非空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系号码不能为空！");
        //判断手机号码格式是否正确
        AssertUtil.isTrue(PhoneUtil.isMobile(linkPhone),"手机号码格式不正确！");
    }

}
