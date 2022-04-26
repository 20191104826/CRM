package com.liu.crm.service;

import com.liu.crm.base.BaseService;
import com.liu.crm.dao.RoleMapper;
import com.liu.crm.utils.AssertUtil;
import com.liu.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 查询所有的角色列表
     *
     * @param userId
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     *  1.参数校验
     *      角色名称  非空，名称唯一
     *  2.设置参数的默认值
     *      是否有效
     *      创建时间
     *      修改时间
     *   3.执行添加操作，判断受影响行数
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
        /* 1. 参数校验 */
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        //通过角色名查询角色记录
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        //判断角色记录是否存在
        //如果角色记录存在，则表示该名称不可用
        AssertUtil.isTrue(temp != null,"角色名称已存在，请重新输入！");
        /* 2.设置参数的默认值 */
        //是否有效
        role.setIsValid(1);
        //创建时间
        role.setCreateDate(new Date());
        //修改时间
        role.setUpdateDate(new Date());
        /* 3.执行添加操作，判断受影响行数 */
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1,"角色添加失败！");
    }



    /**
     * 删除角色
     *      更新isValid设置为0
     * @param ids
     */
//    @Transactional(propagation = Propagation.REQUIRED)
//    public void deleteByIds(Integer[] ids) {
//            //判断ids是否为空，长度是否大于0
//            AssertUtil.isTrue(ids == null || ids.length == 0,"待删除记录不存在!");
//            //执行删除操作，判断受影响行数
//            AssertUtil.isTrue(roleMapper.deleteBatch(ids) != ids.length,"角色删除失败！");

            //遍历用户ID的数组
//            for (Integer roleId : ids){
//                //通过用户ID查询对应的用户角色记录
//                Integer count = roleMapper.selectByPrimaryKey(roleId);
//                //判断用户角色记录是否存在
//                if(count > 0){
//                    //通过用户ID删除对应的用户角色记录
//                    AssertUtil.isTrue(roleMapper.deleteRoleByUserId(roleId) != count,"删除用户失败！");
//                }
//            }

        }
//    }
//}
