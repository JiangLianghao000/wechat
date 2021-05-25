package com.jianglianghao.service;

import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserAndGroupController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.dao.userDao.GroupChatDao;
import com.jianglianghao.dao.userDao.UserAndGroupDao;
import com.jianglianghao.entity.*;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.SensitiveWordUtil;
import com.jianglianghao.util.storageUtils;
import javafx.scene.Group;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/211:17
 */

public class GroupChatService {

    /**
     * 添加群聊消息
     * @param groupChat 实体类，装着消息
     * @throws Exception 异常
     */
    public void addGroupChat(GroupChat groupChat) throws Exception{
        new GroupChatDao().addGroupChat(groupChat);
    }

    /**
     * 找到在群的所有群聊记录
     * @param groupChat 实体类
     * @throws Exception 异常
     * @return
     */
    public List<GroupChat> findAllRecords(GroupChat groupChat)throws Exception{
        return new GroupChatDao().findAllRecords(groupChat);
    }

    /**
     * 删除群聊中所有的记录
     * @param groupChat 实体类
     * @throws Exception 异常
     */
    public void deleteAllGroupRecords(GroupChat groupChat) throws Exception{
        new GroupChatDao().deleteAllGroupRecords(groupChat);
    }

    /**
     * 用户退出群聊
     * @param userInGroup 退出群聊
     * @throws Exception 异常
     */
    public void exitGroup(UserInGroup userInGroup) throws Exception{
        new GroupChatDao().exitGroup(userInGroup);
    }

    /**
     * 踢出群聊
     * @throws Exception 异常
     */
    public int kickOutGroup(String userName, String groupName, HttpServletRequest request) throws Exception{
        //判断是不是群主
        User user = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup = new UserInGroup("userName="+user.getName(), "groupName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        if(userInGroups.size() != 0){
            UserInGroup userInGroup1 = userInGroups.get(0);
            if(!(userInGroup1.getGroupIdentity().equals("群主")||userInGroup1.getGroupIdentity().equals("管理员"))){
                return 2;
            }
            if(userInGroups.size() == 0){
                return 0;
            }else{
                UserInGroup userInGroup2 = new UserInGroup("userName="+userName, "groupName="+groupName);
                return new GroupChatDao().kickOutGroup(userInGroup2);
            }
        }else{
            return -1;
        }
    }

    /**
     * 群主邀请好友添加群聊，查看群主还是普通好友
     * @param friendAccount 用户账号
     * @param groupName 群聊名字
     * @return 结果
     * @throws Exception 异常
     */
    public int inviteFriendAddGroup(String friendAccount, String groupName, HttpServletRequest request) throws Exception{
        //获取群聊消息
        Groups groups = new Groups("groupName="+groupName);
        Groups choiceGroup;
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
        if(allGroups.size() != 0){
            choiceGroup = allGroups.get(0);
        }else{
            return -1;
        }
        //判断身份
        User user = (User) request.getSession().getAttribute("loginUser");
        UserInGroup userInGroupn = new UserInGroup("userId="+user.getId(), "groupName="+groupName);
        List<UserInGroup> userInGroups1 = new UserAndGroupController().findUserInGroups(userInGroupn);
        String userKind = null;
        if(userInGroups1.size() != 0){
            userKind = userInGroups1.get(0).getGroupIdentity();
        }
        //判断账号存不存在
        User findUser = new User("account="+friendAccount);
        List<User> allUser = new UserLoginAndRegistController().getUser(findUser);
        if(allUser.size() == 0){
            //账号不存在
            return 0;
        }
        //账号存在，判断和该用户是不是好友
        UserFriend userFriend = new UserFriend("friendId="+user.getId(), "userId="+allUser.get(0).getId(),
        "state=friend");
        List<UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
        if(userFriends.size() == 0){
            //不是好友
            return 1;
        }
        User friend = allUser.get(0);
        //是好友，判断身份，如果是群主可以直接加，不是群主要等待群主或者管理员同意
        //先判断好友在不在群里面
        UserInGroup checkIfInGroup = new UserInGroup("userId="+friend.getId(), "groupName="+groupName, "isGroupBlacklist=no", "state=inGroup");
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(checkIfInGroup);
        if(userInGroups.size() != 0){
            //证明该用户在群中了
            return 2;
        }
        if(userKind.equals("群主")){
            UserInGroup userInGroup = new UserInGroup(0, friend.getId(), choiceGroup.getGroupAccount(), null, null, "no"
            , "群员", choiceGroup.getGroupName(), friend.getName(), friend.getAccount(), "inGroup", "no", null);
            int i = new UserAndGroupController().addGroup(userInGroup);
            if(i == 1){
                //已邀请进入群聊
                return 3;
            }
        }else{
            //不是群主的其他人
            UserInGroup userInGroup = new UserInGroup(0, friend.getId(), choiceGroup.getGroupAccount(), null, null, "no"
                    , "群员", choiceGroup.getGroupName(), friend.getName(), friend.getAccount(), "wait_for_add", "no", null);
            int i = new UserAndGroupController().addGroup(userInGroup);
            if(i == 1){
                return 4;
            }
        }
        return 5;
    }

    /**
     * 设置群管理员
     * @param groupName 群名
     * @param userName 用户名
     * @param request 请求
     * @return 结果
     * @throws Exception 异常
     */
    public int setToGroupManager(String userName, String groupName, HttpServletRequest request) throws Exception{
        User loginUser = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup1 = new UserInGroup("userId="+loginUser.getId(),"groupName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup1);
        if(userInGroups.size() != 0){
            if(!userInGroups.get(0).getGroupIdentity().equals("群主")){
                //不是群主，不可以设置为群管理员
                return 0;
            }else{
                //是群主
                UserInGroup userInGroup = new UserInGroup("groupIdentity=管理员");
                return new UserAndGroupDao().modifyGroupIdentity(userInGroup, userName, groupName);
            }
        }else{
            return 2;
        }

    }

    /**
     * 取消群管理员身份
     * @param userName 用户名字
     * @param groupName 群名
     * @param request 请求
     * @return 结果
     * @throws Exception 异常
     */
    public int unsetToGroupManager(String userName, String groupName, HttpServletRequest request) throws Exception{
        User loginUser = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup1 = new UserInGroup("userId="+loginUser.getId(),"groupName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup1);
        if(userInGroups.size() != 0){
            if(!userInGroups.get(0).getGroupIdentity().equals("群主")){
                //不是群主，不可以设置为群管理员
                return 0;
            }else{
                //是群主
                UserInGroup userInGroup = new UserInGroup("groupIdentity=群员");
                return new UserAndGroupDao().modifyGroupIdentity(userInGroup, userName, groupName);
            }
        }else{
            return 2;
        }
    }


    /**
     * 取消禁言
     * @param userName 用户名字
     * @param groupName 群聊名字
     * @param request 请求
     * @return 结果
     * @throws Exception 异常
     */
    public int notToSay(String userName, String groupName, HttpServletRequest request) throws Exception {
        User loginUser = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup1 = new UserInGroup("userId="+loginUser.getId(),"groupName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup1);
        if (!((userInGroups.get(0).getGroupIdentity().equals("群主")) || (userInGroups.get(0).getGroupIdentity().equals("管理员")))) {
            //不是群主，不可以设置为群管理员
            return 0;
        }else{
            //是
            UserInGroup userInGroup = new UserInGroup("banSay=yes");
            return new UserAndGroupDao().modifyGroupIdentity(userInGroup, userName, groupName);
        }
    }


    /**
     * 取消禁言
     * @param userName 用户名字
     * @param groupName 群聊名字
     * @param request 请求
     * @return 结果
     * @throws Exception 异常
     */
    public int toSay(String userName, String groupName, HttpServletRequest request) throws Exception{
        User loginUser = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup1 = new UserInGroup("userId="+loginUser.getId(),"groupName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup1);
        if (!((userInGroups.get(0).getGroupIdentity().equals("群主")) || (userInGroups.get(0).getGroupIdentity().equals("管理员")))) {
            //不是群主，不可以设置为群管理员
            return 0;
        }else{
            //是群主
            UserInGroup userInGroup = new UserInGroup("banSay=no");
            return new UserAndGroupDao().modifyGroupIdentity(userInGroup, userName, groupName);
        }
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
        //判断自己是不是管理员或群主，是管理员就可以发公告，不是就不可以
        User loginUser = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup1 = new UserInGroup("userId="+loginUser.getId(),"groupName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup1);
        if (!((userInGroups.get(0).getGroupIdentity().equals("群主")) || (userInGroups.get(0).getGroupIdentity().equals("管理员")))) {
            //不是群主，不可以设置为群管理员
            return 0;
        }else{
            //是群主或管理员，判断有没有敏感词
            //获取所有敏感词集合
            Set<String> badWords = storageUtils.getSensitiveWordsSet();
            //初始化
            SensitiveWordUtil.init(badWords);
            //检测
            String text = content;
            String s = CommonUtil.delHTMLTag(text);
            if (SensitiveWordUtil.contains(s)) {
                //含有敏感词
                return 1;
            }else{
                //不含有敏感词，添加进数据库
                UserInGroup userInGroup = new UserInGroup("announce="+content);
                new UserAndGroupDao().modifyGroupAnnounce(userInGroup, userName, groupName);
                return 2;
            }
        }
    }

    /**
     * 查找表情包
     * @param groupFile 实体类
     * @return 异常
     */
    public List<GroupFile> getMeme(GroupFile groupFile) {
        return new GroupChatDao().getMeme(groupFile);
    }

    /**
     * 添加图片
     * @param groupFile 群文件实体类
     * @throws Exception 异常
     */
    public void addMeme(GroupFile groupFile) throws Exception{
        new GroupChatDao().addMeme(groupFile);
    }
}
