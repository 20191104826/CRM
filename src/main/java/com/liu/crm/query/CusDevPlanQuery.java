package com.liu.crm.query;

import com.liu.crm.base.BaseQuery;

public class CusDevPlanQuery extends BaseQuery {
    private Integer saleChanceId;   //营销机会主键

    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }
}
