package com.liu.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liu.crm.base.BaseService;
import com.liu.crm.dao.CusDevPlanMapper;
import com.liu.crm.dao.SaleChanceMapper;
import com.liu.crm.query.CusDevPlanQuery;
import com.liu.crm.utils.AssertUtil;
import com.liu.crm.vo.CusDevPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Autowired
    private CusDevPlanMapper cusDevPlanMapper;

    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询开发计划
     * 返回的数据格式必须满足 Layui 中数据表格要求的格式
     *
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {

        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        //得到对应的分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));

        //设置 map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());

        //设置分页好的列表
        map.put("data", pageInfo.getList());

        return map;
    }


    /**
     * 添加客户开发计划项数据
     * 1.参数校验
     * 营销机会ID 非空，数据存在
     * 计划项内容  非空
     * 计划时间     非空
     * 2.设置参数的默认值
     * 是否有效  默认有效
     * 创建时间  系统当前时间
     * 修改时间  系统当前时间
     * 3. 执行添加操作，判断受影响的行数
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        //1.参数校验
        checkCusDevPlanParams(cusDevPlan);
        //2.设置默认值
        // 是否有效  默认有效
        cusDevPlan.setIsValid(1);
        //创建时间  系统当前时间
        cusDevPlan.setCreateDate(new Date());
        //修改时间  系统当前时间
        cusDevPlan.setUpdateDate(new Date());

        //3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)!=1,"计划项数据添加失败！");
    }

    /**
     *  修改客户开发计划项数据
     *     1.参数校验
     *          计划项ID    非空，数据存在
     *          营销机会ID 非空，数据存在
     *          计划项内容  非空
     *          计划时间     非空
     *     2.设置参数的默认值
     *          是否有效  默认有效
     *          创建时间  系统当前时间
     *          修改时间  系统当前时间
     *     3. 执行添加操作，判断受影响的行数
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        //1.参数校验
        //计划项ID   非空，数据存在
        AssertUtil.isTrue(null == cusDevPlan.getId()
                || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"数据异常，请重试！");
        checkCusDevPlanParams(cusDevPlan);

        //2.设置默认值
        //修改时间  系统当前时间
        cusDevPlan.setUpdateDate(new Date());

        //3.执行更新操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan)!=1,"计划项更新失败！");

    }

    /**
     * 1.参数校验
     * 营销机会ID 非空，数据存在
     * 计划项内容  非空
     * 计划时间    非空
     *
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //营销机会ID 非空，数据存在
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(null == sId || saleChanceMapper.selectByPrimaryKey(sId) == null, "数据异常，请重试！");

        //计划项内容  非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()), "计划项不能为空！");

        //计划时间    非空
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(), "计划时间不能为空！");
    }
}
