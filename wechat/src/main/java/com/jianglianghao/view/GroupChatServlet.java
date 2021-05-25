package com.jianglianghao.view;

import com.google.gson.Gson;
import com.jianglianghao.controller.GroupChatController;
import com.jianglianghao.controller.UserAndGroupController;
import com.jianglianghao.dao.userDao.GroupChatDao;
import com.jianglianghao.entity.*;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.SensitiveWordUtil;
import com.jianglianghao.util.storageUtils;

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

public class GroupChatServlet extends BaseServlet {

    /**
     * 保存群聊名字进行查询
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void saveGroupName(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String groupName = request.getParameter("groupName");
        request.getSession().setAttribute("findGroupRecord", groupName);
        response.getWriter().write("1");
        return;
    }

    /**
     * 查找所有的群聊记录
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void findGroupRecord(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String groupName = (String)request.getSession().getAttribute("findGroupRecord");
        Groups groups = new Groups("groupName="+groupName);
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
        GroupChat groupChat = new GroupChat("groupAccount="+allGroups.get(0).getGroupAccount());
        List<GroupChat> allRecords = new GroupChatController().findAllRecords(groupChat);
        if(allRecords.size() != 0){
            Gson gson = new Gson();
            String s = gson.toJson(allRecords);
            response.getWriter().write(s);
            return;
        }else{
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 清除所有的群聊消息
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void clearAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String groupName = (String)request.getSession().getAttribute("findGroupRecord");
        Groups groups = new Groups("groupName="+groupName);
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
        if(allGroups.size() !=0 ){
            GroupChat groupChat = new GroupChat("groupAccount="+allGroups.get(0).getGroupAccount());
            new GroupChatController().deleteAllGroupRecords(groupChat);
            response.getWriter().write("1");
        }else{
            response.getWriter().write("0");
        }
    }

    /**
     * 用户退出群聊
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void exitGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String groupName = request.getParameter("groupName");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup = new UserInGroup("groupName="+groupName, "userId="+user.getId());
        new GroupChatController().exitGroup(userInGroup);
        response.getWriter().write("1");
        return;
    }

    /**
     * 把用户踢出群聊
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void kickOutGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String userName = request.getParameter("userName");
        String groupName = request.getParameter("groupName");
        int result = new GroupChatController().kickOutGroup(userName, groupName, request);
        if(result == 0){
            response.getWriter().write("0");
        }
        if(result == 1){
            response.getWriter().write("1");
        }
        if(result == 2){
            response.getWriter().write("2");
        }
    }

    /**
     * 邀请好友
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void inviteFriendAddGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String friendAccount = request.getParameter("friendAccount");
        String groupName = request.getParameter("groupName");
        int i = new GroupChatController().inviteFriendAddGroup(friendAccount, groupName, request);
        response.getWriter().write(String.valueOf(i));
        return;
    }

    /**
     * 设置为群管理员
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void setToGroupManager(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //选择的群用户名字
        String userName = request.getParameter("userName");
        //选择的群聊名字
        String groupName = request.getParameter("groupName");
        int i = new GroupChatController().setToGroupManager(userName, groupName, request);
        response.getWriter().write(String.valueOf(i));
        return;
    }

    /**
     * 取消设置为管理员
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void unsetToGroupManager(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //解除管理员的群员
        String userName = request.getParameter("userName");
        //群名字
        String groupName = request.getParameter("groupName");
        int i = new GroupChatController().unsetToGroupManager(userName, groupName, request);
        response.getWriter().write(String.valueOf(i));
        return;
    }

    /**
     * 禁言
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void notToSay(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //取消禁言的名字
        String userName = request.getParameter("userName");
        //在哪个群
        String groupName = request.getParameter("groupName");
        int i = new GroupChatController().notToSay(userName, groupName, request);
        response.getWriter().write(String.valueOf(i));
        return;
    }

    /**
     * 取消禁言
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void toSay(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //取消禁言的名字
        String userName = request.getParameter("userName");
        //在哪个群
        String groupName = request.getParameter("groupName");
        int i = new GroupChatController().toSay(userName, groupName, request);
        response.getWriter().write(String.valueOf(i));
        return;
    }

    /**
     * 发送群公告
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void addAnnounce(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String content = request.getParameter("announce");
        String groupName = request.getParameter("groupName");
        String userName = request.getParameter("userName");
        int i = new GroupChatController().addAnnounce(content, userName, groupName, request);
        response.getWriter().write(String.valueOf(i));
        return;
    }

    /**
     * 查看是否禁言
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void groupChatServlet(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String userName = request.getParameter("userName");
        String groupName = request.getParameter("groupName");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup = new UserInGroup("userId="+user.getId(), "groupName="+groupName, "userName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        if(userInGroups.size() != 0){
            if(userInGroups.get(0).getBanSay().equals("yes")){
                response.getWriter().write("1");
                return;
            }
        }
        response.getWriter().write("0");
        return;
    }

    /**
     * 查找群公告
     * @param request
     * @param response
     * @throws Exception
     */
    public void findAnnounce(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String groupName = request.getParameter("groupName");
        UserInGroup userInGroup = new UserInGroup("groupName="+groupName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        if(userInGroups.size() != 0){
            response.getWriter().write(new Gson().toJson(userInGroups.get(0).getAnnounce()));
            return;
        }
        response.getWriter().write("0");
        return;
    }

    /**
     * 获取当前账号下该群聊所有表情包
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void getMeme(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String groupName = request.getParameter("groupName");
        String userName = request.getParameter("userName");
        Groups groups = new Groups("groupName="+groupName);
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
        if(allGroups.size() != 0){
            //找到群聊表情包
            GroupFile groupFile = new GroupFile("groupAccount="+allGroups.get(0).getGroupAccount(), "userName="+userName);
            List<GroupFile> meme = new GroupChatController().getMeme(groupFile);
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(meme));
            return;
        }else{
            response.getWriter().write("0");
        }
    }

    /**
     * 查看自己是否被禁言了
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void isBan(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String userName = request.getParameter("userName");
        String groupName = request.getParameter("groupName");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup = new UserInGroup("userId="+user.getId(),"groupName="+groupName, "userName="+userName);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        if(userInGroups.size() == 0){
            response.getWriter().write("0");
            return;
        }else{
            UserInGroup userInGroup1 = userInGroups.get(0);
            if(userInGroup1.getBanSay().equals("yes")){
                response.getWriter().write("1");
                return;
            }
        }
    }
}
