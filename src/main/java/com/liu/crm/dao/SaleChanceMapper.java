package com.liu.crm.dao;

import com.liu.crm.base.BaseMapper;
import com.liu.crm.vo.SaleChance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SaleChanceMapper extends BaseMapper<SaleChance, Integer> {
/**
 * 多条件查询的接口不需要单独定义
 * 由于多个模块涉及到多条件查询，所以将对应的多条件查询功能定义在父接口BaseMappper
 */

}