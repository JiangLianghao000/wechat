package com.jianglianghao.controller;

import com.jianglianghao.dao.userDao.UserAndFriendDao;
import com.jianglianghao.dao.userDao.UserAndGroupDao;
import com.jianglianghao.entity.Groups;
import com.jianglianghao.entity.UserInGroup;
import com.jianglianghao.service.UserAndGroupService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/108:53
 */

public class UserAndGroupController {

    /**
     * 查找用户所在的所有群
     * @param groups 实体类
     * @return list集合
     * @throws Exception 异常
     */
    public List<Groups> findAllGroups(Groups groups) throws Exception {
        return new UserAndGroupService().findAllGroups(groups);
    }

    /**
     * 创建群聊
     * @param groups entity
     * @throws Exception 异常
     */
    public void createGroup(Groups groups) throws Exception{
        new UserAndGroupService().createGroup(groups);
    }

    /**
     * 看看该用户是否在群中
     * @param userInGroup
     * @return
     * @throws Exception
     */
    public List<UserInGroup> CheckIsInGroup(UserInGroup userInGroup) throws Exception{
        return new UserAndGroupService().CheckIsInGroup(userInGroup);
    }

    /**
     * 用户添加群聊
     * @param userInGroup 实体类
     * @throws Exception
     */
    public int addGroup(UserInGroup userInGroup) throws Exception{
        return new UserAndGroupService().addGroup(userInGroup);
    }

    /**
     * 通过联合查询来查询该用户所管理的群
     * @param
     * @return
     */
    public List<UserInGroup> findIsManagerOrGroupBoss(HttpServletRequest request){
        return new UserAndGroupService().findPeopleAddGroup(request);
    }

    /**
     * 在tip界面查找该用户是管理员或者群主的群聊
     * @return list
     */
    public List<UserInGroup> findPeopleAddGroup(HttpServletRequest request){
        return new UserAndGroupService().findPeopleAddGroup(request);
    }

    /**
     * 同意用户进群
     * @param userInGroup
     * @return
     */
    public int userAgreeToInGroup(UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        return new UserAndGroupService().userAgreeToInGroup(userInGroup, request);
    }

    /**
     * 查找用户所在的群
     * @param userInGroup 实体类
     * @return list集合
     * @throws Exception 异常
     */
    public List<UserInGroup> findUserInGroups(UserInGroup userInGroup) throws Exception{
        return new UserAndGroupService().findUserInGroups(userInGroup);
    }

    /**
     * 退出群聊
     * @param userInGroup 实体类
     */
    public int leftGroup(UserInGroup userInGroup, Groups groups) throws Exception{
        return new UserAndGroupService().leftGroup(userInGroup, groups);
    }

    /**
     * 调用数据库查询是否已存在，群名是否相同，是否包含敏感内容
     * @param groups
     * @param userInGroup
     */
    public int mofidyGroupMsg(Groups groups, UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        return new UserAndGroupService().mofidyGroupMsg(groups, userInGroup, request);
    }

    public int mofidyGroupMsg1(UserInGroup userInGroup, HttpServletRequest request) throws Exception {
        return new UserAndGroupService().mofidyGroupMsg1(userInGroup, request);
    }

    /**
     * 修改群头像
     * @param groups
     * @throws Exception
     */
    public void modifyGroupHead(Groups groups, HttpServletRequest request) throws Exception{
        new UserAndGroupService().modifyGroupHead(groups, request);
    }

}
