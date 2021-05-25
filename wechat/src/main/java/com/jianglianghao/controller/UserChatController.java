package com.jianglianghao.controller;

import com.jianglianghao.dao.userDao.UserChatDao;
import com.jianglianghao.entity.UserChat;
import com.jianglianghao.service.UserChatService;

import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1614:11
 */

public class UserChatController {

    /**
     * 添加图片到数据库
     * @param userChat
     * @throws Exception
     */
    public void sendContent(UserChat userChat) throws Exception{
        new UserChatService().sendContent(userChat);
    }

    /**
     * 查找用户和指定好友的的聊天记录
     * @throws Exception 异常
     * @return
     */
    public List<UserChat> getFriendChat(String userName, String friendName) throws Exception{
        return new UserChatService().getFriendChat(userName, friendName);
    }

    /**
     * 删除聊天记录
     * @param userChat 实体类
     * @return 删除结果
     * @throws Exception 异常
     */
    public int deleteRecordMsg(UserChat userChat) throws Exception{
        return new UserChatService().deleteRecordMsg(userChat);
    }
}
