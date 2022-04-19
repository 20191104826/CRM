package com.liu.crm.query;

import com.liu.crm.base.BaseQuery;

/**
 * 营销机会的查询类
 */
public class SaleChanceQuery extends BaseQuery {
    //分页参数

    //条件查询
    private String customerName; //客户名
    private String createMan;    //创建人
    private Integer isState;       //分配状态  0=未分配 ，1=已分配

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getIsState() {
        return isState;
    }

    public void setIsState(Integer isState) {
        this.isState = isState;
    }
}
