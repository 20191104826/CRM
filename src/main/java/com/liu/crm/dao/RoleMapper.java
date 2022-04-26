package com.liu.crm.dao;

import com.liu.crm.base.BaseMapper;
import com.liu.crm.utils.PhoneUtil;
import com.liu.crm.vo.Role;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper extends BaseMapper<Role,Integer> {

    //查询所有的角色列表(只需要id,roleName)
    public List<Map<String,Object>> queryAllRoles(Integer userId);

    //通过角色名查询角色记录
    public Role selectByRoleName(String roleName);
}