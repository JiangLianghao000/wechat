package com.jianglianghao.controller;

import com.jianglianghao.dao.userDao.GroupChatDao;
import com.jianglianghao.entity.GroupChat;
import com.jianglianghao.entity.GroupFile;
import com.jianglianghao.entity.UserInGroup;
import com.jianglianghao.service.GroupChatService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/211:17
 */

public class GroupChatController {

    /**
     * 添加群聊消息
     *
     * @param groupChat 实体类，装着消息
     * @throws Exception 异常
     */
    public void addGroupChat(GroupChat groupChat) throws Exception {
        new GroupChatService().addGroupChat(groupChat);
    }


    /**
     * 找到在群的所有群聊记录
     *
     * @param groupChat 实体类
     * @throws Exception 异常
     */
    public List<GroupChat> findAllRecords(GroupChat groupChat) throws Exception {
        return new GroupChatService().findAllRecords(groupChat);
    }

    /**
     * 删除群聊中所有的记录
     *
     * @param groupChat 实体类
     * @throws Exception 异常
     */
    public void deleteAllGroupRecords(GroupChat groupChat) throws Exception {
        new GroupChatService().deleteAllGroupRecords(groupChat);
    }

    /**
     * 用户退出群聊
     *
     * @param userInGroup 退出群聊
     * @throws Exception 异常
     */
    public void exitGroup(UserInGroup userInGroup) throws Exception {
        new GroupChatService().exitGroup(userInGroup);
    }

    /**
     * 踢出群聊
     *
     * @throws Exception 异常
     */
    public int kickOutGroup(String userName, String groupName, HttpServletRequest request) throws Exception {
        return new GroupChatService().kickOutGroup(userName, groupName, request);
    }

    /**
     * 群主邀请好友添加群聊，查看群主还是普通好友
     *
     * @param friendAccount 用户账号
     * @param groupName     群聊名字
     * @return 结果
     * @throws Exception 异常
     */
    public int inviteFriendAddGroup(String friendAccount, String groupName, HttpServletRequest request) throws Exception {
        return new GroupChatService().inviteFriendAddGroup(friendAccount, groupName, request);
    }

    /**
     * 设置群管理员
     *
     * @param userName  用户名字
     * @param groupName 群名
     * @param request   请求
     * @return 结果
     * @throws Exception 异常
     */
    public int setToGroupManager(String userName, String groupName, HttpServletRequest request) throws Exception {
        return new GroupChatService().setToGroupManager(userName, groupName, request);
    }

    /**
     * 取消群管理员身份
     *
     * @param userName  用户名字
     * @param groupName 群名
     * @param request   请求
     * @return 结果
     * @throws Exception 异常
     */
    public int unsetToGroupManager(String userName, String groupName, HttpServletRequest request) throws Exception {
        return new GroupChatService().unsetToGroupManager(userName, groupName, request);
    }

    /**
     * 禁言
     *
     * @param userName  用户名字
     * @param groupName 群聊名字
     * @param request   请求
     * @return 结果
     * @throws Exception 异常
     */
    public int notToSay(String userName, String groupName, HttpServletRequest request) throws Exception {
        return new GroupChatService().notToSay(userName, groupName, request);
    }

    /**
     * 取消禁言
     *
     * @param userName  用户名字
     * @param groupName 群聊名字
     * @param request   请求
     * @return 结果
     * @throws Exception 异常
     */
    public int toSay(String userName, String groupName, HttpServletRequest request) throws Exception {
        return new GroupChatService().toSay(userName, groupName, request);
    }

    /**
     * 检测用户输入的公告是否合法
     * @param content 内容
     * @param userName 用户名字
     * @param groupName 群聊名字
     * @return 结果
     * @throws Exception 异常
     */
    public int addAnnounce(String content, String userName, String groupName, HttpServletRequest request) throws Exception{
        return new GroupChatService().addAnnounce(content, userName, groupName, request);
    }

    /**
     * 查找表情包
     * @param groupFile 实体类
     * @return 异常
     */
    public List<GroupFile> getMeme(GroupFile groupFile) {
        return new GroupChatService().getMeme(groupFile);
    }

    /**
     * 添加图片
     * @param groupFile 群文件实体类
     * @throws Exception 异常
     */
    public void addMeme(GroupFile groupFile) throws Exception{
        new GroupChatService().addMeme(groupFile);
    }
}
