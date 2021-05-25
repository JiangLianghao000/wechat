package com.jianglianghao.controller;

import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.dao.userDao.UserMsgDao;
import com.jianglianghao.entity.UserFriend;
import com.jianglianghao.entity.UserImformation;
import com.jianglianghao.service.UserAndFriendService;
import com.jianglianghao.service.UserMsgService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/820:26
 */

public class UserMsgController {

    /**
     * 把已经阅读过的修改为konw
     * @param userImformation
     * @return 结果
     * @throws Exception 异常
     */
    public CheckMSG hadRead(UserImformation userImformation, String content,  HttpServletRequest request) throws Exception{
        return new UserMsgService().hadRead(userImformation, content, request);
    }

    /**
     * 删除一条消息
     * @param userImformation 类
     * @param content 消息内容
     * @return 结果
     * @throws Exception
     */
    public CheckMSG deleteMessage(UserImformation userImformation, String content) throws Exception{
        return new UserMsgService().deleteMessage(userImformation, content);
    }

    /**
     * 检测有没有敏感信息
     * @param text 反馈信息
     * @throws Exception
     */
    public int checkTextArea(String text) throws Exception{
        return new UserMsgService().checkTextArea(text);
    }

    /**
     * 反馈信息
     * @param userImformation 实体类
     * @return 结果
     * @throws Exception
     */
    public int addMsgInFeedback(UserImformation userImformation) throws Exception{
        return new UserMsgService().addMsgInFeedback(userImformation);
    }

    /**
     * 修改头像
     * @param userFriend
     * @throws Exception
     */
    public void modifyHead(UserFriend userFriend, int userId, int friendId) throws Exception{
        new UserMsgService().modifyHead(userFriend, userId, friendId);
    }
}
