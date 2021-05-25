package com.jianglianghao.dao.userDao;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.UserChat;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1614:11
 */

public class UserChatDao {

    /**
     * 添加图片到数据库
     * @param userChat
     * @throws Exception
     */
    public void sendContent(UserChat userChat) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.add(userChat);
    }

    /**
     * 查找用户和指定好友的的聊天记录
     * @throws Exception 异常
     * @return
     */
    public List<UserChat> getFriendChat(String userName, String friendName) throws Exception{
        String sql = "select id id, user_name userName, friend_name friendName, content content, msg_type msgType from user_chat where (user_name= ? and friend_name = ? ) or (friend_name=? and user_name=?)";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<UserChat> allInstances = instance.getAllInstances(UserChat.class, sql, userName, friendName, userName, friendName);
        return allInstances;
    }

    /**
     * 删除聊天记录
     * @param userChat 实体类
     * @return 删除结果
     * @throws Exception 异常
     */
    public int deleteRecordMsg(UserChat userChat) throws Exception{
        UserDaoImpl instance = UserDaoImpl.getInstance();
        instance.delete(userChat);
        return 1;
    }
}
