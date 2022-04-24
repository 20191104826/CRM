package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import com.liu.crm.base.ResultInfo;
import com.liu.crm.exceptions.ParamsException;
import com.liu.crm.model.UserModel;
import com.liu.crm.query.UserQuery;
import com.liu.crm.service.UserService;
import com.liu.crm.utils.LoginUserUtil;
import com.liu.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody //前台是ajax请求，表示会返回一个json的格式给客户端
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        //调用service层登录方法
        UserModel userModel = userService.userLogin(userName, userPwd);

        //设置ResultInfo的result的值，将数据返回给请求
        resultInfo.setResult(userModel);

        //通过try catch 捕获service层的异常，如果service层抛出异常，则登录失败，否则登录成功
//        try {
//            //调用service层登录方法
//            UserModel userModel = userService.userLogin(userName, userPwd);
//
//            //设置ResultInfo的result的值，将数据返回给请求
//            resultInfo.setResult(userModel);
//        }catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败！");
//        }
        return resultInfo;
    }

    /**
     * 用户修改密码
     *
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,
                                         String oldPassword, String newPassword, String repeatPassword) {
        ResultInfo resultInfo = new ResultInfo();

        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用Service层修改用户密码
        userService.updatePassword(userId, oldPassword, newPassword, repeatPassword);

//        try{
//            //获取cookie中的userId
//            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
//            //调用Service层修改用户密码
//            userService.updatePassword(userId,oldPassword,newPassword,repeatPassword);
//
//        }catch (ParamsException p){
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch (Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败！");
//            e.printStackTrace();
//        }

        return resultInfo;
    }

    /**
     * 进入修改密码的页面
     *
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {

        return "user/password";
    }

    /**
     * 查询所有的销售人员
     *
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    /**
     * 分页多条件查询用户列表
     *
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(UserQuery userQuery) {
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }


    /**
     * 添加一个用户
     *
     * @param user
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        return success("用户添加成功！");
    }

    /**
     * 打开添加或修改页面
     *
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id, HttpServletRequest request) {
        //判断用户ID是否为空，不为空表示更新操作，查询用户对象
        if (id != null) {
            User user = userService.selectByPrimaryKey(id);
            //将数据设置到请求域中
            request.setAttribute("user", user);
        }
        return "user/add_update";
    }


    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("更新用户成功！");
    }

    /**
     * 删除用户，可以多条删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {
        userService.deleteByIds(ids);
        return success("用户删除成功！");
    }
}
