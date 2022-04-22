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
     *      linkPhone   联系号码      非空，手机号码格式正确
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
     * 更新营销机会
     *  1.参数校验
     *      营销机会ID 非空，数据库中对应的数据存在
     *      customerName 客户名称，   非空
     *      linkMan     联系人       非空
     *      linkPhone   联系号码      非空，手机号码格式正确
     *  2.设置相关参数的默认值
     *      updateDate更新时间  设置为当前系统时间
     *      assignMan 指派人
     *          原始数据未设置
     *              修改后未设置
     *                   不需要操作
     *              修改后已设置
     *                  assignTime 指派时间，设置为系统当前时间
     *                  分配状态  1=已分配
     *                  开发状态  1=开发中
     *          原始数据已设置
     *              修改后未设置
     *                  assignTime 设置为null
     *                  分配状态  0=未分配
     *                  开发状态  0=未开发
     *              修改后已设置
     *                  判断修改前后指派人是否是同一个
     *                      如果是，则不需要操作
     *                      如果不是，则需要更新 assignTime 指派时间 设置位系统当前时间
     *   3.执行更新操作，判断受影响行数
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance){
        /**1.参数校验 */
        //营销机会ID 非空 , 数据库中对应的数据存在
        AssertUtil.isTrue(null == saleChance.getId(),"待更新记录不存在！");
        //通过主键查询对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断数据库中对应的数据存在
        AssertUtil.isTrue(temp == null,"待更新记录不存在！");
        //参数校验
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        /* 2.设置相关参数的默认值 */
       // updateDate更新时间  设置为当前系统时间
        saleChance.setUpdateDate(new Date());
        // assignMan 指派人
        //判断原始数据是否存在
        if(StringUtils.isBlank(temp.getAssignMan())){ //不存在
            //判断修改后的值是否存在
            if(!StringUtils.isBlank(saleChance.getAssignMan())){   //修改前为空，修改后存在
                //assignTime 指派时间，设置为系统当前时间
                saleChance.setAssignTime(new Date());
                //分配状态  1=已分配
                saleChance.setIsState(StateStatus.STATED.getType());
                //开发状态  1=开发中
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        }else{   //存在
            //判断修改后的值是否存在
            if(StringUtils.isBlank(saleChance.getAssignMan())){  //修改前有值，修改后无值
                //assignTime 设置为null
                saleChance.setAssignTime(null);
                //分配状态  0=未分配
                saleChance.setIsState(StateStatus.UNSTATE.getType());
                //开发状态  0=未开发
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else{  //修改前有值，修改后有值
                //判断修改前后是否是同一个指派人
                if(!saleChance.getAssignMan().equals(temp.getAssignMan())){ //更新前后不是同一个指派人
                    //更新指派时间
                    saleChance.setAssignTime(new Date());
                }else{ //修改前后是同一个人
                    //设置指派时间为修改前的时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
        // 3.执行更新操作，判断受影响行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"更新营销机会失败！");
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
