package com.jianglianghao.controller;

import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.entity.User;
import com.jianglianghao.service.UserLoginAndRegistService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/3021:00
 */

public class UserLoginAndRegistController {

    /**
     *  对用户信息进行检测
     * @param user
     * @return
     * @throws Exception
     */
    public CheckMSG checkUserMSG(User user) throws Exception {
        return new UserLoginAndRegistService().checkMSG(user);
    }

    //对用户登陆检测
    public CheckMSG userLogin(User user, HttpServletRequest req) throws Exception{
        return new UserLoginAndRegistService().userLogin(user, req);
    }

    /**
     * 对用户注册管理
     * @param user 用户
     * @return
     * @throws Exception 异常
     */
    public CheckMSG userRegist(User user) throws Exception{
        return new UserLoginAndRegistService().userRegist(user);
    }

    /**
     * 修改用户信息
     * @param user 用户类
     * @return 修改结果
     * @throws Exception
     */
    public int modifyUserMSG(User user) throws  Exception{
        return new UserLoginAndRegistService().modifyUserMSG(user);
    }

    /**
     * 获取user对象
     * @param user 传入的user
     * @return 返回user集合
     * @throws Exception 异常
     */
    public List<User> getUser(User user) throws Exception {
        return new UserLoginAndRegistService().getUser(user);
    }

    /**
     * 用户个人信息界面修改信息用的
     * @param user
     * @return
     */
    public CheckMSG modifyMSG(User user, HttpServletRequest request) throws Exception {
        return new UserLoginAndRegistService().modifyMSG(user, request);
    }



}
