package com.liu.crm.service;

import com.liu.crm.base.BaseService;
import com.liu.crm.dao.UserMapper;
import com.liu.crm.model.UserModel;
import com.liu.crm.utils.AssertUtil;
import com.liu.crm.utils.Md5Util;
import com.liu.crm.utils.UserIDBase64;
import com.liu.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Integer> {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * 1.参数判断，判断用户姓名，用户密码非空
     * 如果参数为空，抛出异常（异常被控制层捕获并处理）
     * 2.调用数据访问层，通过用户名查询用户记录，返回用户对象
     * 3.判断用户对象是否为空
     * 如果对象为空，抛出异常（异常被控制层捕获并处理）
     * 4.判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
     * 如果密码不相等，抛出异常（异常被控制层捕获并处理）
     * 5.如果密码正确，登录成功
     */
    public UserModel userLogin(String userName, String userPwd) {
        //  1.参数判断，判断用户姓名，用户密码非空
        checkLoginParams(userName, userPwd);
        // 2.调用数据访问层，通过用户名查询用户记录，返回用户对象
        User user = userMapper.queryUserByName(userName);
        // 3.判断用户对象是否为空
        AssertUtil.isTrue(user == null, "用户姓名不存在！");
        // 4.判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码
        checkUserPwd(userPwd, user.getUserPwd());
        // 5.返回构建对象
        return buildUserInfo(user);
    }

    /**
     * 修改密码
     * 1.通过用户ID，查询用户记录，返回用户对象
     * 2.参数校验
     * 待更新用户记录是否存在（判断用户对象是否为空）
     * 判断原始密码是否为空
     * 判断原始密码是否正确（查询的用户对象中的用户密码与原始密码是否一致）
     * 判断新密码是否为空
     * 判断新密码是否与原始密码一致（不允许一致）
     * 判断确认密码是否为空
     * 判断确认密码是否与新密码一致
     * 3.设置用户的新密码
     * 需要将新密码通过指定的算法进行加密（MD5加密）
     * 4.执行更新操作，判断受影响的行数
     *
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        //通过用户ID，查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 判断用户记录是否存在
        AssertUtil.isTrue(null == user,"待更新记录不存在！");

        //参数校验
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
        //设置用户新密码
        user.setUserPwd(Md5Util.encode(newPwd));

        //执行更新操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改密码失败！");

    }

    /**
     * 修改密码的参数校验
     * 待更新用户记录是否存在（判断用户对象是否为空）
     *      * 判断原始密码是否为空
     *      * 判断原始密码是否正确（查询的用户对象中的用户密码与原始密码是否一致）
     *      * 判断新密码是否为空
     *      * 判断新密码是否与原始密码一致（不允许一致）
     *      * 判断确认密码是否为空
     *      * 判断确认密码是否与新密码一致
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空！");
        //判断原始密码是否正确（查询的用户对象中的用户密码与原始密码是否一致）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确！");
        // 判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空！");
        //判断新密码是否与原始密码一致（不允许一致）
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原始密码相同！");
        //判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空!");
        //判断确认密码是否与新密码一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致!");
    }


    /**
     * 构建需要返回给客户端的用户对象
     *
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
//        userModel.setUserId(user.getId());
//        设置加密过的用户id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码判断
     * 先将客户端传递的密码加密，再与数据库中查询到的密码进行比较
     *
     * @param userPwd
     * @param pwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        //将客户端传递的密码按照一定规则加密
        userPwd = Md5Util.encode(userPwd);
        //判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(pwd), "用户密码不正确！");
    }

    /**
     * 1.参数判断
     * 如果参数为空，抛出异常（异常被控制层捕获并处理）
     *
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        //验证用户名,如果是真的就抛出异常
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空！");
    }

    /**
     * 查询所有销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }
}
