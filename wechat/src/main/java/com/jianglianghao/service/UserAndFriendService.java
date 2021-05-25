package com.jianglianghao.service;

import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.dao.userDao.UserAndFriendDao;
import com.jianglianghao.dao.userDao.UserAndGroupDao;
import com.jianglianghao.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/622:10
 */

public class UserAndFriendService {

    /**
     * 获取查找的用户消息
     * @param userFriend 查找的用户
     * @return list集合
     * @throws Exception
     */
    public List<UserFriend> getUserFriends(UserFriend userFriend) throws Exception {
        return new UserAndFriendDao().getUserFriend(userFriend);
    }

    public void addFriend(UserFriend userFriend) throws Exception{
        new UserAndFriendDao().addFriend(userFriend);
    }

    /**
     * 获取用户所有的好友
     * @param userFriend
     * @return
     * @throws Exception
     */
    public List<UserFriend> getAllFriend(UserFriend userFriend) throws Exception{
        return new UserAndFriendDao().getAllFriend(userFriend);
    }

    /**
     * 获取用户的所有通知信息
     * @param userImformation 用户消息实体类
     * @return
     * @throws Exception
     */
    public List<UserImformation> getAllMessage(UserImformation userImformation) throws Exception{
        return new UserAndFriendDao().getAllMessage(userImformation);
    }

    /**
     * 获取好友请求信息
     * @return 返回list
     * @throws Exception 异常
     */
    public List<UserInGroup> getFriendApplyMsg (HttpServletRequest request) throws Exception{
        return new UserAndGroupDao().findPeopleAddGroup(request);
    }

    /**
     * 同意添加好友
     * @param userFriend
     * @return
     * @throws Exception
     */
    public int userAgreeAddForFriend(UserFriend userFriend, HttpServletRequest request) throws Exception{
        return new UserAndFriendDao().userAgreeAddForFriend(userFriend, request);
    }

    /**
     * 拉黑好友
     * @throws Exception 异常
     */
    public int addToBlackList(UserFriend userFriend,  HttpServletRequest request) throws Exception{
        return new UserAndFriendDao().addToBlackList(userFriend, request);
    }

    /**
     * 取消拉黑好友
     * @throws Exception 异常
     */
    public int NotAllToBlackList(UserFriend userFriend, HttpServletRequest request) throws Exception{
        return new UserAndFriendDao().NotAllToBlackList(userFriend, request);
    }

    /**
     * 修改备注
     * @param userFriend 实体类
     * @return 修改结果
     * @throws Exception 异常
     */
    public int modifyNote(UserFriend userFriend, HttpServletRequest request) throws Exception{
        return new UserAndFriendDao().modifyNote(userFriend, request);
    }

    /**
     * 删除好友
     * @param userFriend 实体类
     * @throws Exception 异常
     */
    public int deleteFriend(UserFriend userFriend) throws Exception{
        return new UserAndFriendDao().deleteFriend(userFriend);
    }

    /**
     * 游客拉黑好友
     * @param userFriend 好友类
     * @param request 请求
     * @throws Exception 异常
     */
    public int travelAddToBlacklist(UserFriend userFriend, HttpServletRequest request, String friendName) throws Exception{
        return new UserAndFriendDao().travelAddToBlacklist(userFriend, request, friendName);
    }

    /**
     * 游客取消拉黑好友
     * @param userFriend 好友类
     * @param request 请求
     * @throws Exception 异常
     */
    public int travelNotAddToBlacklist(UserFriend userFriend, HttpServletRequest request, String friendName) throws Exception{
        return new UserAndFriendDao().travelNotAddToBlacklist(userFriend, request, friendName);
    }

    /**
     * 发送好友卡片
     * @param card 好友卡片
     * @throws Exception 异常
     */
    public int sendCard(Card card) throws Exception{
        //发送人
        String sendPeople = card.getSendPeople();
        //被发送给谁
        String beSendedPeople = card.getBeSendedPeople();
        //卡片上的人
        String cardPeople = card.getCardPeople();
        //判断被发送的人和卡片上的人两个人是不是相同的
        if(beSendedPeople.equals(cardPeople)){
            return 1;
        }
        //判断是不是好友关系
        User user = new User("name="+beSendedPeople);
        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        if(user1.size() != 0){
            UserFriend userFriend = new UserFriend("friendId="+user1.get(0).getId(), "friendName="+ beSendedPeople, "state=friend");
            List<UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriend);
            if(allFriend.size() != 0){
                //已经是好友关系
                return 2;
            }else{
                new UserAndFriendDao().sendCard(card);
                return 3;
            }
        }else{
            return -1;
        }

    }

    /**
     * 查看好友卡片
     * @param card 好友卡片
     * @throws Exception 异常
     */
    public List<Card> findAllCards(Card card) throws Exception{
        return new UserAndFriendDao().findAllCards(card);
    }

    /**
     * 删除卡片
     * @param card 卡片实体类
     * @throws Exception 异常
     */
    public void deleteCard(Card card) throws Exception{
        new UserAndFriendDao().deleteACard(card);
    }
}
