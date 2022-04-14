package com.liu.crm.controller;

import com.liu.crm.base.BaseController;
import com.liu.crm.service.UserService;
import com.liu.crm.utils.LoginUserUtil;
import com.liu.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class indexController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 系统登录页
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 系统界面欢迎页
     *
     * @return
     */
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 后端管理主页面
     *
     * @return
     */
    @RequestMapping("/main")
    public String main(HttpServletRequest request) {
        //获取cookie中的用户id
        Integer userid = LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象，设置session作用域
        User user = userService.selectByPrimaryKey(userid);
        request.getSession().setAttribute("user", user);
        return "main";
//        内部跳转用的是转发，转发可以用作用域去传递
//        request一次请求，再次点击超链接之后，请求就会失效
        //this is a test
    }
}
