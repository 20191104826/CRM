package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import com.liu.crm.base.ResultInfo;
import com.liu.crm.exceptions.ParamsException;
import com.liu.crm.model.UserModel;
import com.liu.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody //前台是ajax请求，表示会返回一个json的格式给客户端
    public ResultInfo userLogin(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();

        //通过try catch 捕获service层的异常，如果service层抛出异常，则登录失败，否则登录成功
        try {
            //调用service层登录方法
            UserModel userModel = userService.userLogin(userName, userPwd);

            //设置ResultInfo的result的值，将数据返回给请求
            resultInfo.setResult(userModel);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("登录失败！");
        }
        return resultInfo;
    }
}
