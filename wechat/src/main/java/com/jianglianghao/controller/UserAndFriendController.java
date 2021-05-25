package com.jianglianghao.controller;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.dao.userDao.UserAndFriendDao;
import com.jianglianghao.entity.*;
import com.jianglianghao.service.UserAndFriendService;
import com.jianglianghao.service.UserLoginAndRegistService;
import com.sun.media.jfxmedia.events.NewFrameEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/622:02
 */

public class UserAndFriendController {

    /**
     * 查找添加用户好友
     * @param userFriend
     * @return
     * @throws Exception
     */
    public List<UserFriend> getUserFriends(UserFriend userFriend) throws Exception {
        return new UserAndFriendService().getUserFriends(userFriend);
    }

    /**
     * 把用户信息添加入数据库
     * @param userFriend
     * @throws Exception
     */
    public void addFriend(UserFriend userFriend) throws Exception{
        new UserAndFriendService().addFriend(userFriend);
    }

    /**
     * 获取用户所有的好友
     * @param userFriend
     * @return
     * @throws Exception
     */
    public List<UserFriend> getAllFriend(UserFriend userFriend) throws Exception{
        return new UserAndFriendService().getAllFriend(userFriend);
    }

    /**
     * 获取用户的所有通知信息
     * @param userImformation 用户消息实体类
     * @return
     * @throws Exception
     */
    public List<UserImformation> getAllMessage(UserImformation userImformation) throws Exception{
        return new UserAndFriendService().getAllMessage(userImformation);
    }

    /**
     * 获取好友请求信息
     * @param userFriend entity类
     * @return 返回list
     * @throws Exception 异常
     */
    public List<UserFriend> getFriendApplyMsg (UserFriend userFriend) throws Exception{
        return new UserAndFriendDao().getFriendApplyMsg(userFriend);
    }

    /**
     * 同意添加好友
     * @param userFriend
     * @return
     * @throws Exception
     */
    public int userAgreeAddForFriend(UserFriend userFriend, HttpServletRequest request) throws Exception{
        return new UserAndFriendService().userAgreeAddForFriend(userFriend, request);
    }

    /**
     * 拉黑好友
     * @throws Exception 异常
     */
    public int addToBlackList(UserFriend userFriend, HttpServletRequest request) throws Exception{
        return new UserAndFriendService().addToBlackList(userFriend, request);
    }

    /**
     * 取消拉黑好友
     * @throws Exception 异常
     */
    public int NotAllToBlackList(UserFriend userFriend, HttpServletRequest request) throws Exception{
        return new UserAndFriendService().NotAllToBlackList(userFriend, request);
    }

    /**
     * 修改备注
     * @param userFriend 实体类
     * @return 修改结果
     * @throws Exception 异常
     */
    public int modifyNote(UserFriend userFriend, HttpServletRequest request) throws Exception{
        return new UserAndFriendService().modifyNote(userFriend, request);
    }

    /**
     * 删除好友
     * @param userFriend 实体类
     * @throws Exception 异常
     */
    public int deleteFriend(UserFriend userFriend) throws Exception{
        return new UserAndFriendService().deleteFriend(userFriend);
    }

    /**
     * 游客拉黑好友
     * @param userFriend 好友类
     * @param request 请求
     * @throws Exception 异常
     */
    public int travelAddToBlacklist(UserFriend userFriend, HttpServletRequest request, String friendName) throws Exception{
        return new UserAndFriendService().travelAddToBlacklist(userFriend, request, friendName);
    }

    /**
     * 游客取消拉黑好友
     * @param userFriend 好友类
     * @param request 请求
     * @throws Exception 异常
     */
    public int travelNotAddToBlacklist(UserFriend userFriend, HttpServletRequest request, String friendName) throws Exception{
        return new UserAndFriendService().travelNotAddToBlacklist(userFriend, request, friendName);
    }

    /**
     * 发送好友卡片
     * @param card 好友卡片
     * @throws Exception 异常
     */
    public int sendCard(Card card) throws Exception{
        return new UserAndFriendService().sendCard(card);
    }


    /**
     * 查看好友卡片
     * @param card 好友卡片
     * @throws Exception 异常
     */
    public List<Card> findAllCards(Card card) throws Exception{
        return new UserAndFriendService().findAllCards(card);
    }

    /**
     * 删除卡片
     * @param card 卡片实体类
     * @throws Exception 异常
     */
    public void deleteCard(Card card) throws Exception{
        new UserAndFriendService().deleteCard(card);
    }

}
