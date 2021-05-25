package com.jianglianghao.view;
import com.jianglianghao.entity.*;
import com.jianglianghao.view.BaseServlet;
import com.google.gson.Gson;
import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserAndGroupController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.entity.User;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.StringUtil;
import com.sun.javafx.sg.prism.web.NGWebView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jianglianghao.util.storageUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/523:11
 */

public class UserAndFriendServlet extends BaseServlet {

    /**
     * 用户搜索群聊或者好友
     *
     * @param request  请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void findFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user2 = (User)request.getSession().getAttribute("loginUser");
        String name = request.getParameter("name");
        String account = request.getParameter("account");
        String choice = request.getParameter("searchType");//获取选择搜索的方法，群聊或者用户
        if (CommonUtil.judgeMSG(name) == false && CommonUtil.judgeMSG(account) == false) {
            //用户两种方式都选择了
            request.setAttribute("findmsg", "不能同时根据两种方式查找");
            //跳转回本页面
            request.getRequestDispatcher("/pages/mainView/search.jsp").forward(request, response);
            return;
        }
        //如果没有，就证明只选择了一个
        //如果是选择了查找群聊
        if (choice.equals("群聊")) {
            //1. 查找群聊，2. 查看是否被拉黑 3。 查看是否在这个群聊中了 4. 查看是否在等待添加的过程中
            //判断是不是输入全是null
            if (CommonUtil.judgeMSG(account) == true && CommonUtil.judgeMSG(name) == true) {
                //用户没有输入信息就查找了
                request.setAttribute("findmsg", "请输入信息再查找");
                //跳转回本页面
                request.getRequestDispatcher("/pages/mainView/search.jsp").forward(request, response);
                return;
            }
            //判断是用了名字搜索还是账号搜索
            if (CommonUtil.judgeMSG(account) == false) {
                //通过群号查找
                com.jianglianghao.entity.Groups groups = new com.jianglianghao.entity.Groups("groupAccount=" + account);
                //通过groups查找
                List<com.jianglianghao.entity.Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
                //进行判断
                if (allGroups.size() == 0) {
                    //如果是0， 证明没有找到
                    request.setAttribute("findmsg", "没有找到该群，请检测输入是否正确");
                    //跳转回本页面
                    request.getRequestDispatcher("/pages/mainView/search.jsp").forward(request, response);
                    return;
                } else {
                    //证明通过账号找到了群号，把群聊放入attribute中
                    request.setAttribute("findGroup", allGroups.get(0));
                    //先清除，再存入查找的群聊
                    request.getSession().setAttribute("findGroup", allGroups.get(0));
                    //判断在群的状态
                    com.jianglianghao.entity.UserInGroup userInGroup = new com.jianglianghao.entity.UserInGroup("userId=" + user2.getId(), "groupAccount=" + account);
                    List<com.jianglianghao.entity.UserInGroup> userInGroups = new UserAndGroupController().CheckIsInGroup(userInGroup);
                    if (userInGroups.size() == 0) {
                        //证明没有在群中或者没有和该群有相关的记录
                        request.setAttribute("userSearchGroup", "notYourFriend");
                        request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                        return;
                    } else {
                        //判断是什么状态
                        if (userInGroups.get(0).getState().equals("inGroup")) {
                            //在群中
                            if (userInGroups.get(0).getIsGroupBlacklist().equals("yes")) {
                                //被拉黑了
                                request.setAttribute("userSearchGroup", "已被拉黑，不得再加入此群");
                                request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                                return;
                            } else {
                                //证明在群中
                                request.setAttribute("userSearchGroup", "你已加群");
                                request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                                return;
                            }
                        }

                        if (userInGroups.get(0).getState().equals("wait_for_add")) {
                            request.setAttribute("userSearchGroup", "等待同意加入");
                            request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                            return;
                        }
                    }
                }
            } else {
                //通过群名称查找
                com.jianglianghao.entity.Groups groups = new com.jianglianghao.entity.Groups("groupName=" + name);
                //通过groups查找
                List<com.jianglianghao.entity.Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
                //进行判断
                if (allGroups.size() == 0) {
                    //如果是0， 证明没有找到
                    request.setAttribute("findmsg", "没有找到该群，请检测输入是否正确");
                    //跳转回本页面
                    request.getRequestDispatcher("/pages/mainView/search.jsp").forward(request, response);
                    return;
                } else {
                    //证明通过账号找到了群号，把群聊放入attribute中
                    request.setAttribute("findGroup", allGroups.get(0));
                    //调用方法查找群主名
                    List<User> user = new UserLoginAndRegistController().getUser(new User("id=" + allGroups.get(0).getUserId()));
                    request.setAttribute("GroupTopProple", user.get(0));
                    //先清除，再存入查找的群聊
                    request.getSession().setAttribute("findGroup", allGroups.get(0));
                    //判断在群的状态
                    com.jianglianghao.entity.UserInGroup userInGroup = new com.jianglianghao.entity.UserInGroup("userId=" + user2.getId(), "groupName=" + name);
                    List<com.jianglianghao.entity.UserInGroup> userInGroups = new UserAndGroupController().CheckIsInGroup(userInGroup);
                    if (userInGroups.size() == 0) {
                        //证明没有在群中或者没有和该群有相关的记录
                        request.setAttribute("userSearchGroup", "notYourFriend");
                        request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                        return;
                    } else {
                        //判断是什么状态
                        if (userInGroups.get(0).getState().equals("inGroup")) {
                            //在群中
                            if (userInGroups.get(0).getIsGroupBlacklist().equals("yes")) {
                                //被拉黑了
                                request.setAttribute("userSearchGroup", "已被拉黑，不得再加入此群");
                                request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                                return;
                            } else {
                                //证明在群中
                                request.setAttribute("userSearchGroup", "你已加群");
                                request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                                return;
                            }
                        }

                        if (userInGroups.get(0).getState().equals("wait_for_add")) {
                            request.setAttribute("userSearchGroup", "等待同意加入");
                            request.getRequestDispatcher("/pages/mainView/groupMsg.jsp").forward(request, response);
                            return;
                        }
                    }
                }
            }
        }
        //如果是选择了查找用户
        if (choice.equals("用户")) {
            //判断是不是输入全是null
            if (CommonUtil.judgeMSG(account) == true && CommonUtil.judgeMSG(name) == true) {
                //用户没有输入信息就查找了
                request.setAttribute("findmsg", "请输入信息再查找");
                //跳转回本页面
                request.getRequestDispatcher("/pages/mainView/search.jsp").forward(request, response);
                return;
            }
            //判断是用了名字搜索还是账号搜索
            if (CommonUtil.judgeMSG(account) == false) {
                //通过账号查找
                User user = new User("account=" + account);
                List<User> users = new UserLoginAndRegistController().getUser(user);
                if (users.size() == 0) {
                    //证明没有找到指定的用户
                    //用户两种方式都选择了
                    request.setAttribute("findmsg", "没有找到用户，请继检测输入是否正确");
                    //跳转回本页面
                    request.getRequestDispatcher("/pages/mainView/search.jsp").forward(request, response);
                    return;
                } else {
                    //找到了该用户，把用户放进session中
                    request.getSession().setAttribute("findUser", users.get(0));
                    //先清除，再存入查找的用户
                    request.getSession().setAttribute("findFriendMsg", users.get(0));
                    //判断你和该用户是否是好友关系
                    com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("userId=" + user2.getId(), "friendId=" + users.get(0).getId());
                    List<com.jianglianghao.entity.UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
                    if (userFriends.size() == 0) {
                        //两个人不是好友,存储信息，不是你的好友，这时候显示添加好友按钮
                        request.getSession().setAttribute("userSearchFriend", "notYourFriend");
                        response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                        return;
                    } else {
                        //如果这个状态是等待添加，就代表了已经加过一次为好友，如今在等待对方同意
                        if (userFriends.get(0).getState().equals("wait_for_add")) {
                            request.getSession().setAttribute("userSearchFriend", "等待对方加你为好友");
                            response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                            return;
                        }
                        //不是等待添加状态，就要判断是否被拉黑
                        //两个人是好友关系，就要判断是否被拉黑了，拉黑就提示一被拉黑，不拉黑就显示已经是好友
                        //getIsBlacklist:yes:拉黑 no: 没拉黑
                        String isBlacklist = userFriends.get(0).getIsBlacklist();
                        if (isBlacklist.equals("yes")) {
                            //拉黑了，就显示已被拉黑
                            //两个人不是好友,存储信息，不是你的好友
                            request.getSession().setAttribute("userSearchFriend", "已被拉黑或者拉黑了对方");
                            response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                            return;
                        } else {
                            request.getSession().setAttribute("userSearchFriend", "你和对方已经是好友了");
                            response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                            return;
                        }
                    }
                }
            } else {
                //通过名字查找，和上面的逻辑一样
                //通过名字查找
                User user = new User("name=" + name);
                List<User> users = new UserLoginAndRegistController().getUser(user);
                if (users.size() == 0) {
                    //证明没有找到指定的用户
                    //用户两种方式都选择了
                    request.setAttribute("findmsg", "没有找到用户，请检测输入是否正确");
                    //跳转回本页面
                    request.getRequestDispatcher("/pages/mainView/search.jsp").forward(request, response);
                    return;
                } else {
                    //找到了该用户，把用户放进session中
                    request.getSession().setAttribute("findUser", users.get(0));
                    //先清除，再存入查找的用户
                    request.getSession().setAttribute("findFriendMsg", users.get(0));
                    //判断你和该用户是否是好友关系
                    com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("userId=" + user2.getId(), "friendId=" + users.get(0).getId());
                    List<com.jianglianghao.entity.UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
                    if (userFriends.size() == 0) {
                        //两个人不是好友,存储信息，不是你的好友，这时候显示添加好友按钮
                        request.getSession().setAttribute("userSearchFriend", "notYourFriend");
                        response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                        return;
                    } else {
                        //如果这个状态是等待添加，就代表了已经加过一次为好友，如今在等待对方同意
                        if (userFriends.get(0).getState().equals("wait_for_add")) {
                            request.getSession().setAttribute("userSearchFriend", "等待对方加你为好友");
                            response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                            return;
                        }
                        //不是等待添加状态，就要判断是否被拉黑
                        //两个人是好友关系，就要判断是否被拉黑了，拉黑就提示一被拉黑，不拉黑就显示已经是好友
                        //getIsBlacklist:true:拉黑 false: 没拉黑
                        String isBlacklist = userFriends.get(0).getIsBlacklist();
                        if (isBlacklist.equals("yes")) {
                            //拉黑了，就显示已被拉黑
                            //两个人不是好友,存储信息，不是你的好友
                            request.getSession().setAttribute("userSearchFriend", "已被拉黑或者拉黑了对方");
                            response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                            return;
                        } else {
                            request.getSession().setAttribute("userSearchFriend", "你和对方已经是好友了");
                            response.sendRedirect(request.getContextPath() + "/pages/mainView/friendMsg.jsp");
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 用户搜索用户点击添加好友后调用该方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void addFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user1 = (User)request.getSession().getAttribute("loginUser");
        //调用数据库，把用户和好友同时添加进去
        //获取friend的各种消息，以及用户的各种消息
        //好友的消息
        com.jianglianghao.entity.User friend = (User)request.getSession().getAttribute("findFriendMsg");
        //用户
        com.jianglianghao.entity.User user = user1;
        //自己一个，userid设置为添加人， friendid设置为被添加人，表明是useId加对方FriendId
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("userId=" + user.getId(), "friendId=" + friend.getId(),
                "friendName=" + user.getName(), "friendAccount=" + user.getAccount(), "isBlacklist=no",
                "email=" + user.getEmail(), "ciclePermission=yes", "friendHeadportrait=" + user.getHeadProtrait(),
                "state=wait_for_add");
        //调用方法存入dao
        new UserAndFriendController().addFriend(userFriend);
        response.getWriter().write("1");
    }

    /**
     * 用于查询该用户的所有的好友
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getAllFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        request.getSession().setAttribute("mainView_action", "choiceFriend");
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("friendId=" + user.getId(), "state=friend");
        //获取list集合
        List<com.jianglianghao.entity.UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriend);
        if (allFriend.size() == 0) {
            //集合里面没有数据，证明没有好友，直接重定向
            response.getWriter().write("0");
            return;
        }
        request.getSession().setAttribute("findAllFriends", allFriend);
        return;
    }

    /**
     * 获取所有的通知消息，包括公告/不包括群公告
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getAllMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        request.getSession().setAttribute("mainView_action", "choiceAllMsg");
        com.jianglianghao.entity.UserImformation userImformation = new com.jianglianghao.entity.UserImformation("userId=" + user.getId());
        List<com.jianglianghao.entity.UserImformation> allMessage = new UserAndFriendController().getAllMessage(userImformation);
        if (allMessage.size() == 0) {
            //没有消息，返回0
            response.getWriter().write("0");
            return;
        } else {
            //有消息
            request.getSession().setAttribute("allUserMsgList", allMessage);
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * 获取好友请求信息
     *
     * @param request
     * @param response
     * @throws Exception 异常
     */
    public void getFriendApplyMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        //获取数据库中的信息
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("friendId=" + user.getId(), "state=wait_for_add");
        //获取
        List<com.jianglianghao.entity.UserFriend> friendApplyMsg = new UserAndFriendController().getFriendApplyMsg(userFriend);
        if (friendApplyMsg.size() != 0) {
            request.setAttribute("lists", friendApplyMsg);
            request.setAttribute("choiceAddTipByWhatWay", "byUser");
            request.getRequestDispatcher("/pages/mainView/fiendAddTip.jsp").forward(request, response);
            //跳转
            return;
        }
        request.getRequestDispatcher("/pages/mainView/fiendAddTip.jsp").forward(request, response);
        return;
    }

    /**
     * 在Tip界面通过账号查询好友
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void findUserByAccountInTipJsp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accout1 = request.getParameter("account");
        request.getSession().setAttribute("InTipJspUserContent", accout1);
        //分解
        String[] split = accout1.split(":");
        List<User> user = new UserLoginAndRegistController().getUser(new User("account=" + split[0]));
        //放入session中
        if (user.size() != 0) {
            request.getSession().setAttribute("findUserByAccountInFriendTipJsp", user.get(0));
            response.getWriter().write("1");
        }
        return;
    }

    /**
     * 同意添加好友
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void userAgreeAddForFriend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user2 = (User)request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        //同意添加好友
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("state=friend");
        int i = new UserAndFriendController().userAgreeAddForFriend(userFriend, request);
        //设置自己作为对方的好友
        com.jianglianghao.entity.User user = (User)request.getSession().getAttribute("loginUser");
        com.jianglianghao.entity.UserFriend userFriend1 = new com.jianglianghao.entity.UserFriend(0, user.getId(), user2.getId(), user.getName(), null,
                user.getAccount(), "no", user.getEmail(), "yes", user.getHeadProtrait(), "friend");
        new UserAndFriendController().addFriend(userFriend1);
        if (i == 1) {
            response.getWriter().write("1");
            return;
        } else {
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 点击好友信息显示出来
     * @param request
     * @param response
     * @throws Exception
     */
    public void findFriendMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{

        User user2= (User)request.getSession().getAttribute("loginUser");
        //获取账号
        String friendAccount = request.getParameter("friendAccount");
        //获取账号的group
        User user = new User("account="+friendAccount);
        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        //存储
        request.getSession().setAttribute("findFriendUser", user1.get(0));
        User user3 = (User)request.getSession().getAttribute("findFriendUser");
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("friendId="+user2.getId(), "userId="+user3.getId());
        List<com.jianglianghao.entity.UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
        request.getSession().setAttribute("UserFriend", userFriends.get(0));
        response.getWriter().write("1");
        return;
    }

    /**
     * 拉黑好友
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void addToBlackList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取好友
        User user1 = (User)request.getSession().getAttribute("findFriendUser");
        User user = (User)request.getSession().getAttribute("loginUser");
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("isBlacklist=yes");
        int i = new UserAndFriendController().addToBlackList(userFriend,request);
        if(i == 1){
            //修改成功了
            //更新 findUserFriend
            com.jianglianghao.entity.UserFriend userFriend1 = new com.jianglianghao.entity.UserFriend("friendId="+user.getId(), "userId="+user1.getId());
            List<com.jianglianghao.entity.UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend1);
            request.getSession().setAttribute("UserFriend", userFriends.get(0));
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * 取消拉黑好友
     * @param request 请求
     * @param response 转发
     * @throws Exception 异常
     */
    public void NotAllToBlackList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        User user1 = (User)request.getSession().getAttribute("findFriendUser");
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("isBlacklist=no");
        int i = new UserAndFriendController().NotAllToBlackList(userFriend, request);
        if(i == 1){
            //修改成功了
            //更新 findUserFriend
            com.jianglianghao.entity.UserFriend userFriend1 = new com.jianglianghao.entity.UserFriend("friendId="+user.getId(), "userId="+user1.getId());
            List<com.jianglianghao.entity.UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend1);
            request.getSession().setAttribute("UserFriend", userFriends.get(0));
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * 修改备注
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void modifyNote(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user1 = (User)request.getSession().getAttribute("findFriendUser");
        User user = (User)request.getSession().getAttribute("loginUser");
        String note = request.getParameter("note");
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("friendNote="+note);
        int i = new UserAndFriendController().modifyNote(userFriend, request);
        if(i == 1){
            //修改成功了
            //更新 findUserFriend
            com.jianglianghao.entity.UserFriend userFriend1 = new com.jianglianghao.entity.UserFriend("friendId="+user.getId(), "userId="+user1.getId());
            List<com.jianglianghao.entity.UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend1);
            request.getSession().setAttribute("UserFriend", userFriends.get(0));
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * 删除好友
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void deleteFriend(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user1 = (User)request.getSession().getAttribute("findFriendUser");
        User user = (User)request.getSession().getAttribute("loginUser");
        com.jianglianghao.entity.UserFriend userFriend = new com.jianglianghao.entity.UserFriend("userId="+user.getId(), "friendId="+user1.getId());
        com.jianglianghao.entity.UserFriend userFriend2 = new com.jianglianghao.entity.UserFriend("friendId="+user.getId(), "userId="+user1.getId());
        int i = new UserAndFriendController().deleteFriend(userFriend);
        int i1 = new UserAndFriendController().deleteFriend(userFriend2);
        if(i == 1 && i1 == 1){
            //删除了好友
            response.getWriter().write("1");
            //重新刷新页面，赋值
            com.jianglianghao.entity.UserFriend userFriend1 = new com.jianglianghao.entity.UserFriend("friendId=" + user.getId(), "state=friend");
            //获取list集合
            List<com.jianglianghao.entity.UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriend1);
            request.getSession().setAttribute("findAllFriends", allFriend);
            return;
        }
    }

    /**
     * 初始化好友列表
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void addFriendsInCardView(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        UserFriend userFriend = new UserFriend("friendId="+ user.getId());
        List<UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
        if(userFriends.size() != 0){
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(userFriends));
            return;
        }else{
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 添加卡片
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void sendCard(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //发送给谁
        String beSendedPeople = request.getParameter("sendPeople");
        //发送谁
        String cardPeople = request.getParameter("beSendPeople");
        //发送人
        String sendPeople = ((User)request.getSession().getAttribute("loginUser")).getName();
        Card card = new Card(0, sendPeople, beSendedPeople, cardPeople);
        int i = new UserAndFriendController().sendCard(card);
        response.getWriter().write(String.valueOf(i));
        return;
    }

    /**
     * 找到所有卡片
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void findAllCards(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User)request.getSession().getAttribute("loginUser");
        Card card = new Card("beSendedPeople="+user.getName());
        List<Card> allCards = new UserAndFriendController().findAllCards(card);
        if(allCards.size() != 0){
            response.getWriter().write(new Gson().toJson(allCards));
            return;
        }else{
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 通过卡片添加好友
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void addFriendByUsingCard(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String friendName = request.getParameter("friendName");
        User user = (User)request.getSession().getAttribute("loginUser");
        int userId = user.getId();
        List<User> user1 = new UserLoginAndRegistController().getUser(new User("name=" + friendName));
        if(user1.size() != 0){
            User friend = user1.get(0);
            UserFriend userFriend = new UserFriend(0, user.getId(), friend.getId(), user.getName(), null, user.getAccount(),
                    "no", user.getEmail(), "yes", user.getHeadProtrait(), "friend");
            UserFriend userFriend1 = new UserFriend(0,friend.getId(),  user.getId(), friend.getName(), null, friend.getAccount(),
                    "no", friend.getEmail(), "yes", friend.getHeadProtrait(), "friend");
            new UserAndFriendController().addFriend(userFriend);
            new UserAndFriendController().addFriend(userFriend1);
            //销毁一张卡片
            Card card = new Card("beSendedPeople="+user.getName(), "cardPeople="+friend.getName());
            new UserAndFriendController().deleteCard(card);
            response.getWriter().write("1");
            return;
        }else{
            response.getWriter().write("0");
            return;
        }
    }
}
