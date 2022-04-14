package com.liu.crm.dao;

import com.liu.crm.base.BaseMapper;
import com.liu.crm.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User, Integer> {
    // 通过用户名查询用户记录，返回用户对象
    public User queryUserByName(String userName);
}