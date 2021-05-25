package com.jianglianghao.view;

import com.google.gson.Gson;
import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserAndGroupController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.entity.Groups;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserInGroup;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.SensitiveWordUtil;
import com.jianglianghao.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.jianglianghao.util.storageUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 用户和群聊的操作
 * @verdion
 * @date 2021/5/108:53
 */
import com.jianglianghao.view.BaseServlet;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class UserAndGroupServlet extends BaseServlet {

    /**
     * 主界面找到所有的群聊消息
     *
     * @param request
     * @param response
     * @throws Exception 异常
     */
    public void findAllGroups(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup = new UserInGroup("userId=" + user.getId());
        //查找该id所在的群
        List<UserInGroup> allGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        if (allGroups.size() == 0) {
            response.getWriter().write("0");
            return;
        } else {
            //存入req域中
            request.getSession().setAttribute("mainView_action", "choiceAllGroupsMsg");
            request.getSession().setAttribute("allGroupMsgList", allGroups);
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * Tip界面找群聊消息
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void findAllGroupAddMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //查找添加群的人
        List<UserInGroup> peopleAddGroup = new UserAndGroupController().findPeopleAddGroup(request);
        //遍历添加进list
        if (peopleAddGroup.size() != 0) {
            //申请消息不为0
            Gson gson = new Gson();
            String s = gson.toJson(peopleAddGroup);
            response.getWriter().write(s);
            return;
        } else {
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 创建群聊
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void createGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user2 = (User)request.getSession().getAttribute("loginUser");
        String groupAccount = request.getParameter("account");
        String groupName = request.getParameter("name");
        //对account和name判断
        if (CommonUtil.judgeMSG(groupAccount) == true || CommonUtil.judgeMSG(groupName) == true) {
            //其中至少有一个是空
            request.setAttribute("exceotipn", "群号和群名至少有一个是空");
            request.getRequestDispatcher("/pages/mainView/createGroup.jsp").forward(request, response);
            return;
        }
        //如果不是有一个为空，用正则表达式判断
        String accountRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}";
        String nameRegex = "^.{3,20}$";
        if (groupAccount.matches(accountRegex) == false) {
            //如果账号不合法
            request.setAttribute("exceotipn", "账号不合法");
            request.getRequestDispatcher("/pages/mainView/createGroup.jsp").forward(request, response);
            return;
        }
        if (groupName.matches(nameRegex) == false) {
            //如果群名不合法
            request.setAttribute("exceotipn", "群名不合法");
            request.getRequestDispatcher("/pages/mainView/createGroup.jsp").forward(request, response);
            return;
        }
        //对群聊名字和账号进行检测，查看是否已经存在
        Groups group = new Groups("groupAccount=" + groupAccount);
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(group);
        Groups group1 = new Groups("groupName=" + groupName);
        List<Groups> allGroups1 = new UserAndGroupController().findAllGroups(group1);
        //检测是否存在
        if (allGroups.size() != 0) {
            //如果群名不合法
            request.setAttribute("exceotipn", "已存在账号");
            request.getRequestDispatcher("/pages/mainView/createGroup.jsp").forward(request, response);
            return;
        }
        if (allGroups1.size() != 0) {
            //如果群账号不合法
            request.setAttribute("exceotipn", "已存在群名字");
            request.getRequestDispatcher("/pages/mainView/createGroup.jsp").forward(request, response);
            return;
        }
        //接下来对群简洁进行敏感词检测
        //首先获取所有的敏感词
        Set<String> badWords = getSensitiveWordsSet();
        //获取text
        String text = request.getParameter("jianjies");
        if (CommonUtil.judgeMSG(text) == true) {
            //如果text不合法
            request.setAttribute("exceotipn", "简介为空");
            request.getRequestDispatcher("pages/mainView/createGroup.jsp").forward(request, response);
            return;
        }
        //先初始化
        SensitiveWordUtil.init(badWords);
        //调用方法检测
        boolean result = SensitiveWordUtil.contains(text);
        if (result == true) {
            //证明有敏感词汇
            //如果账号不合法
            request.setAttribute("exceotipn", "简介含有敏感词汇");
            request.getRequestDispatcher("pages/mainView/createGroup.jsp").forward(request, response);
            return;
        }
        //排除以上情况，可以进行添加数据库
        Groups groups = new Groups(0, user2.getId(), groupAccount, groupName, null, text);
        new UserAndGroupController().createGroup(groups);
        //把创建的人添加进user_in_group表中
        UserInGroup userInGroup = new UserInGroup(0, user2.getId(), groupAccount, null,
                null, "no", "群主", groupName, user2.getName(), user2.getAccount(), "inGroup", "no", null);
        new UserAndGroupController().addGroup(userInGroup);
        request.setAttribute("exceotipn", "已创建");
        request.getRequestDispatcher("pages/mainView/createGroup.jsp").forward(request, response);
        return;
    }

    /**
     * 添加群
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void addGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user2 = (User)request.getSession().getAttribute("loginUser");
        Groups groups = (Groups)request.getSession().getAttribute("findGroup");
        UserInGroup userInGroup = new UserInGroup(0, user2.getId(), groups.getGroupAccount()
                , null, null, "no", "群员", groups.getGroupName(), user2.getName(), user2.getAccount(), "wait_for_add", "no", null);
        int i = new UserAndGroupController().addGroup(userInGroup);
        if (i == 1) {
            response.getWriter().write("1");
        } else {
            response.getWriter().write("0");
        }
        return;
    }

    /**
     * 同意用户进群
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void userAgreeToInGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //修改用户状态，可以进群
        String inTipJspUserContent = (String)request.getSession().getAttribute("InTipJspUserContent");
        String[] split = inTipJspUserContent.split(":");
        UserInGroup userInGroup = new UserInGroup("state=inGroup", "groupName="+split[1]);
        int i = new UserAndGroupController().userAgreeToInGroup(userInGroup, request);
        if (i == 1) {
            response.getWriter().write("1");
        } else {
            response.getWriter().write("0");
        }
    }

    /**
     * 主界面点击群聊
     *
     * @param request  请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void findGroupInMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupAccount = request.getParameter("groupAccount");
        //通过群号查群消息
        Groups groups = new Groups("groupAccount=" + groupAccount);
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
        if (allGroups.size() == 0) {
            response.getWriter().write("0");
            return;
        }else{
            //找到了群
            request.getSession().setAttribute("findUserByAccountInFriendTipJsp", allGroups.get(0));
            //通过群id找群主
            User user = new User("id=" + allGroups.get(0).getUserId());
            List<User> user1 = new UserLoginAndRegistController().getUser(user);
            request.getSession().setAttribute("groupMainPeople", user1.get(0).getName());
            response.getWriter().write("1");
            return;
        }
    }

    /**
     * 退出群聊
     * @param request
     * @param response
     * @throws Exception
     */
    public void leftGroup(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Groups groups1 = (Groups)request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        User user = (User)request.getSession().getAttribute("loginUser");
        UserInGroup userInGroup = new UserInGroup("userId="+user.getId(), "groupAccount=" + groups1.getGroupAccount());
        Groups groups = new Groups("group_account" + groups1.getGroupAccount());
        int i = new UserAndGroupController().leftGroup(userInGroup, groups);
        if(i == 1){
            response.getWriter().write("1");
            return;
        }else{
            response.getWriter().write("0");
            return;
        }
    }

    /**
     * 修改群聊消息
     * @param request
     * @param response
     * @throws Exception
     */
    public void mofidyGroupMsg(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Groups groups2 = (Groups)request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
        User user = (User)request.getSession().getAttribute("loginUser");
        String name = request.getParameter("name");
        //群昵称
        String nickname = request.getParameter("nickName");
        String textArea = request.getParameter("text");
        //用户在群的昵称
        String userNickName = request.getParameter("userNickName");
        if(user.getId() == groups2.getUserId()){
            //证明是群主
            if(nickname.equals("")){
                nickname = null;
            }
            if(userNickName.equals("")){
                userNickName = null;
            }
            if(textArea.equals("")){
                textArea =groups2.getGroupAnnounce();
            }
            Groups groups = new Groups("groupName="+name, "groupAnnounce="+textArea);
            UserInGroup userInGroup = new UserInGroup("groupNickname=" + nickname, "userNickname=" + userNickName);
            //调用数据库方法查询
            int i = new UserAndGroupController().mofidyGroupMsg(groups, userInGroup, request);
            if(i == 6){
                //找到该群的所有消息
                Groups fbi = (Groups) request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
                Groups groups1 = new Groups("groupAccount="+fbi.getGroupAccount());
                List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups1);
                request.getSession().setAttribute("findUserByAccountInFriendTipJsp", allGroups.get(0));
            }
            response.getWriter().write(i+"");
            return;
        }else{
            //证明不是群主,这时候不能对群昵称和群简介修改了
            UserInGroup userInGroup = new UserInGroup("groupNickname=" + nickname, "userNickname=" + userNickName);
            //调用数据库方法查询是否已存在群名了
            int i = new UserAndGroupController().mofidyGroupMsg1( userInGroup, request);
            response.getWriter().write(i+"");
            if(i == 9){
                Groups fbi = (Groups) request.getSession().getAttribute("findUserByAccountInFriendTipJsp");
                Groups groups1 = new Groups("groupAccount="+fbi.getGroupAccount());
                List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups1);
                request.getSession().setAttribute("findUserByAccountInFriendTipJsp", allGroups.get(0));
            }
            return;
        }
    }
}
