package com.jianglianghao.view;

import com.google.gson.Gson;
import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserAndGroupController;
import com.jianglianghao.controller.UserChatController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.dao.userDao.UserAndFriendDao;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserChat;
import com.jianglianghao.entity.UserFriend;
import com.jianglianghao.entity.UserInGroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1614:10
 */

public class UserChatServlet extends com.jianglianghao.view.BaseServlet {

    /**
     * 进入聊天室
     * @param request 请求
     * @param response 转发
     * @throws Exception 异常
     */
    public void intoChat(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        //找到所有的好友
        UserFriend userFriend = new UserFriend("friendId="+user.getId(), "state=friend", "isBlacklist=no");
        List<UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriend);
        //存入req中
        request.getSession().setAttribute("findAllFriendsInFriendsChat", allFriend);
        //打开聊天室
        request.getRequestDispatcher("/pages/mainView/userChatView.jsp").forward(request, response);
        return;
    }

    /**
     * 进入聊天室
     * @param request 请求
     * @param response 转发
     * @throws Exception 异常
     */
    public void travelIntoChat(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        //找到所有的好友
        UserFriend userFriend = new UserFriend("friendId="+user.getId(), "state=friend", "isBlacklist=no");
        List<UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriend);
        //存入req中
        request.getSession().setAttribute("findAllFriendsInFriendsChat", allFriend);
        //打开聊天室
        request.getRequestDispatcher("/pages/mainView/userChatView.jsp").forward(request, response);
        return;
    }

    /**
     * 获取登陆的用户的名字
     * @param request
     * @param response
     * @throws Exception
     */
    public void getName(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        response.getWriter().write( user.getName());
        return;
    }

    /**
     * 获取登陆的用户的头像
     * @param request
     * @param response
     * @throws Exception
     */
    public void getHead(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        response.getWriter().write( user.getHeadProtrait());
        return;
    }

    public void getNickName(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String friendName = request.getParameter("friendname");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserFriend userFriend = new UserFriend("friendId="+user.getId(), "friendName="+friendName);
        List<UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
        response.getWriter().write(userFriends.get(0).getFriendNote());
    }

    /**
     * 用户点击的时候开始搜索聊天记录
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void getFriendChat(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String friendName1 = (String) request.getSession().getAttribute("userFindFriendNameInCharRoom");
        User user = (User)request.getSession().getAttribute("loginUser");
        String userName = user.getName();
        List<UserChat> friendChat = new UserChatController().getFriendChat(userName, friendName1);
        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(friendChat));
        return;
    }

    /**
     * 获取好友头像
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void getfriendHead(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String friendName = request.getParameter("friendName");
        List<User> user = new UserLoginAndRegistController().getUser(new User("name=" + friendName));
        response.getWriter().write(user.get(0).getHeadProtrait());
        return;
    }

    /**
     * 保存用户名字
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void saveFriendName(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String name = request.getParameter("friendName");
        if(name == null || name.equals("") || name.equals("undefine")){
            response.getWriter().write("0");
            return;
        }else{
            request.getSession().setAttribute("userFindFriendNameInCharRoom", name);
            response.getWriter().write("1");
            return;
        }
    }

    public void deleteAllChatMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String friendName1 = (String) request.getSession().getAttribute("userFindFriendNameInCharRoom");
        if(friendName1 == null || friendName1.equals("") || friendName1.equals("undefine")) {
            response.getWriter().write("0");
            return;
        }else{
            String username =((User)request.getSession().getAttribute("loginUser")).getName();
            UserChat userChat = new UserChat("userName="+username, "friendName="+friendName1);
            UserChat userChat1 = new UserChat("userName="+friendName1, "friendName="+username);
            int i = new UserChatController().deleteRecordMsg(userChat);
            int i1 = new UserChatController().deleteRecordMsg(userChat1);
            if(i == 1 && i1 == 1){
                response.getWriter().write("1");
                return;
            }
        }
    }

    /**
     * 获取当前用户的所有的群聊
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void getAllGroups(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup = new UserInGroup("userName="+loginUser.getName(), "state=inGroup", "isGroupBlacklist=no");
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        //获取到所有的群聊消息
        if(userInGroups.size() == 0){
            //证明没有群聊
            response.getWriter().write("0");
            return;
        }else{
            response.getWriter().write(new Gson().toJson(userInGroups));
            return;
        }
    }

    /**
     * 找出所有在群聊的用户
     * @param request
     * @param response
     * @throws Exception
     */
    public void findAllPeopleInGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String groupName = request.getParameter("groupName");
        UserInGroup userInGroup = new UserInGroup("groupName="+groupName, "state=inGroup", "isGroupBlacklist=no");
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        if(userInGroups.size() == 0){
            response.getWriter().write("0");
            return;
        }else{
            response.getWriter().write(new Gson().toJson(userInGroups));
            return;
        }
    }

    /**
     * 找出群聊的备注
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void findGroupNickName(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        String groupName = request.getParameter("groupName");
        UserInGroup userInGroup = new UserInGroup("groupName="+groupName, "userName="+user.getName());
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        if(userInGroups.size() != 0){
            response.getWriter().write(userInGroups.get(0).getGroupNickname());
            return;
        }
    }
}
