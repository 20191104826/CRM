package com.liu.crm.service;

import com.liu.crm.base.BaseService;
import com.liu.crm.dao.UserRoleMapper;
import com.liu.crm.vo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Autowired
    private UserRoleMapper userRoleMapper;
}
