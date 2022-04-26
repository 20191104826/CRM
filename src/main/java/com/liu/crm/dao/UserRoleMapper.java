package com.liu.crm.dao;

import com.liu.crm.base.BaseMapper;
import com.liu.crm.vo.UserRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //根据用户ID查询用户角色记录
    Integer countUserRoleByUserId(Integer userId);
    //根据用户ID删除用户角色记录
    Integer deleteUserRoleByUserId(Integer userId);
}