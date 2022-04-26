package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import com.liu.crm.base.ResultInfo;
import com.liu.crm.query.RoleQuery;
import com.liu.crm.service.RoleService;
import com.liu.crm.vo.Role;
import com.liu.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 查询所有的角色
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    /**
     * 分页查询角色列表
     *
     * @param roleQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 进入角色管理页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /**
     * 添加角色操作
     *
     * @param role
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addRole(Role role){
        roleService.addRole(role);
        return success("角色添加成功！");
    }

    /**
     * 进入 添加 /更新 角色 页面
     * @return
     */
    @RequestMapping("toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage(Integer id, HttpServletRequest request){
        //判断用户ID是否为空，不为空表示更新操作，查询用户对象
        if (id != null) {
            Role role = roleService.selectByPrimaryKey(id);
            //将数据设置到请求域中
            request.setAttribute("role", role);
        }
        return "role/add_update";
    }

//    /**
//     * 删除角色
//     *  将is_valid设置为0
//     * @param ids
//     * @return
//     */
//    @RequestMapping("delete")
//    @ResponseBody
//    public ResultInfo deleteRoles(Integer[] ids){
//        roleService.deleteByIds(ids);
//        return success("角色删除成功！");
//    }

}
