package com.jianglianghao.view;

import com.google.gson.Gson;
import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserFriend;
import com.jianglianghao.util.CommonUtil;
import org.codehaus.jackson.map.Serializers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/2113:47
 */

public class TravelServlet extends BaseServlet {

    /**
     * 查找所有游客的好友
     * @param req
     * @param resp
     * @throws Exception
     */
    public void getTravelFriends(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User user = (User)req.getSession().getAttribute("loginUser");
        String name = user.getName();
        UserFriend userFriend = new UserFriend("friendId="+user.getId(), "state=friend");
        List<UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
        if(userFriends.size()!=0){
            Gson gson = new Gson();
            String s = gson.toJson(userFriends);
            resp.getWriter().write(s);
            return;
        }else{
            resp.getWriter().write("0");
            return;
        }
    }

    /**
     * 通过用户名字查找消息
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void getFriend(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String name = req.getParameter("friendName");
        User user = new User("name="+name);
        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        if(user1.size() == 0){
            //没找到
            resp.getWriter().write("0");
            return;
        }else{
            req.getSession().setAttribute("travelFindFriend", user1.get(0));
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 拉黑好友
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void addToBlackList(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //获取用户好友
        String name = req.getParameter("friendName");
        if(name == null || name.equals("undefined")){
            resp.getWriter().write("0");
            return;
        }
        //先查看有没有被拉黑过
        User user = (User)req.getSession().getAttribute("loginUser");
        UserFriend userFriendTest = new UserFriend("userId="+user.getId(), "friendName="+name, "isBlacklist=yes", "state=friend");
        List<UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriendTest);
        if(allFriend.size() != 0){
            resp.getWriter().write("2");
            return;
        }
        UserFriend userFriend = new UserFriend("isBlacklist=yes");
        int i = new UserAndFriendController().travelAddToBlacklist(userFriend, req, name);
        if(i == 1){
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 用户取消拉黑
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void notAddToBlacklist(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //获取用户好友
        String name = req.getParameter("friendName");
        if(name == null || name.equals("undefined")){
            resp.getWriter().write("0");
            return;
        }
        User user = (User)req.getSession().getAttribute("loginUser");
        UserFriend userFriendTest = new UserFriend("userId="+user.getId(), "friendName="+name, "isBlacklist=no", "state=friend");
        List<UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriendTest);
        if(allFriend.size() != 0){
            resp.getWriter().write("2");
            return;
        }
        UserFriend userFriend = new UserFriend("isBlacklist=no");
        int i = new UserAndFriendController().travelAddToBlacklist(userFriend, req, name);
        if(i == 1){
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 游客查找好友
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void trivelFindFriend(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //用户好友
        String friendAccount = req.getParameter("friendAccount");
        if(friendAccount == null || friendAccount.equals("")){
            resp.getWriter().write("0");
            return;
        }
        User user2 = null;
        //获取登陆的用户
        User user = (User)req.getSession().getAttribute("loginUser");
        List<User> user1 = new UserLoginAndRegistController().getUser(new User("account=" + friendAccount));
        if(user1.size() != 0){
            //获取查找的好友
            user2 = user1.get(0);
            UserFriend userFriend = new UserFriend(0, user.getId(), user2.getId(), user.getName(), null, user.getAccount(),
                    "no", user.getEmail(), "yes", user.getHeadProtrait(), "wait_for_add");
            //看看有没有已经等待同意的
            UserFriend userFriend1 = new UserFriend("userId="+user.getId(), "friendId="+user2.getId(), "state=wait_for_add");
            List<UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend1);
            if(userFriends.size() != 0){
                resp.getWriter().write("3");
                return;
            }else{
                new UserAndFriendController().addFriend(userFriend);
                resp.getWriter().write("1");
                return;
            }

        }else{
            resp.getWriter().write("2");
            return;
        }
    }

    /**
     * 游客删除好友
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void triveldeleteFriend(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //用户好友
        String friendName = req.getParameter("friendName");
        User user = (User)req.getSession().getAttribute("loginUser");
        List<User> user1 = new UserLoginAndRegistController().getUser(new User("name=" + friendName));
        //第一个记录
        UserFriend userFriend = new UserFriend("friendId="+user.getId(), "friendName="+friendName);
        new UserAndFriendController().deleteFriend(userFriend);
        if(user1.size() != 0){
            UserFriend userFriend1 = new UserFriend("friendId="+user1.get(0).getId(), "friendName="+user.getName());
            new UserAndFriendController().deleteFriend(userFriend1);
        }
        resp.getWriter().write("1");
    }

    /**
     * 通过账号查找
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void findFriend(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String friendName = req.getParameter("friendName");
        if(CommonUtil.judgeMSG(friendName)== true){
            resp.getWriter().write("0");
            return;
        }else {
            User user = new User("account="+friendName);
            List<User> user1 = new UserLoginAndRegistController().getUser(user);
            if(user1.size() == 0){
                //找不到
                resp.getWriter().write("1");
                return;
            }else{
                req.getSession().setAttribute("travelFindFriend", user1.get(0));
                resp.getWriter().write("2");
                return;
            }
        }
    }
}
