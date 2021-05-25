package com.jianglianghao.controller;

import com.jianglianghao.dao.userDao.ManagerDao;
import com.jianglianghao.entity.Ban;
import com.jianglianghao.entity.Circle;
import com.jianglianghao.entity.User;
import com.jianglianghao.service.ManagerService;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1521:23
 */

public class ManagerController {

    /**
     * 获取所有用户在管理员界面展示
     * @return list集合
     * @throws Exception 异常
     */
    public List<User> getAllUser() throws Exception{
        return new ManagerService().getAllUser();
    }

    /**
     * 对用户解封
     * @param ban 实体类
     * @return 结果
     * @throws Exception 异常
     */
    public int bandUser(Ban ban) throws Exception{
        return new ManagerService().bandUser(ban);
    }

    /**
     * 对用户解封
     * @param ban 实体类
     * @return 结果
     * @throws Exception 异常
     */
    public int unBandUser(Ban ban) throws Exception{
        return new ManagerService().unBandUser(ban);
    }

    /**
     * 删除朋友圈
     * @param circle 朋友圈实体类
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteCircle(Circle circle) throws Exception{
        return new ManagerService().deleteCircle(circle);
    }

    /**
     * 去除管理员权限
     * @param user 用户实体类
     * @param account 账号，根据账号修改权限
     * @return 修改结果
     * @throws Exception 异常
     */
    public int banManager(User user, String account)throws Exception{
        return new ManagerService().banManager(user, account);
    }

    /**
     * 找到被封号的人的所有消息
     * @param ban
     * @return
     */
    public List<Ban> finaBan(Ban ban) throws Exception {
        return new ManagerDao().finaBan(ban);
    }
}
