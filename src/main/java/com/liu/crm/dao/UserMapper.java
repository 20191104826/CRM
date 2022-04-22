package com.liu.crm.dao;

import com.liu.crm.base.BaseMapper;
import com.liu.crm.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User, Integer> {
    // 通过用户名查询用户记录，返回用户对象
    public User queryUserByName(String userName);

    //查询所有的销售人员
    List<Map<String,Object>> queryAllSales();
}