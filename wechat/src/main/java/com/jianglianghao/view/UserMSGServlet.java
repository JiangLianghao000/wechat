package com.jianglianghao.view;

import com.google.gson.Gson;
import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.controller.UserMsgController;
import com.jianglianghao.dao.pool.AbstractMyDataSource;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserImformation;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.PasswordUtil;
import org.junit.Test;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 用户信息界面的修改的操作
 * @verdion
 * @date 2021/5/310:27
 */

//支持文件上传，用于上传头像
@MultipartConfig
public class UserMSGServlet extends com.jianglianghao.view.BaseServlet {

    /**
     * 获取头像
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void modifyMSG(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user1 = (User)request.getSession().getAttribute("loginUser");
        //对用户名和密码和邮箱可以进行修改
        //用户名
        String name = request.getParameter("name");
        //密码
        String password = request.getParameter("password");
        //邮箱
        String email = request.getParameter("email");
        //首先对用户名和密码和邮箱进行非空检测
        if(CommonUtil.judgeMSG(name) || CommonUtil.judgeMSG(password) || CommonUtil.judgeMSG(email)){
            //其中有一个是正确，就说明有一个为null，不可修改
            request.setAttribute("msg", "输入的信息有空值");
            request.getRequestDispatcher("/pages/mainView/userSetting.jsp").forward(request, response);
            return;
        }
        //都不为null，就分别判断是否正确，用户名要和数据库比较
        User user = new User("name=" + name, "email=" + email ,"password=" + password);
        //先不设置密码，最后检测完之后才加密
        CheckMSG checkMSG = new UserLoginAndRegistController().modifyMSG(user, request);
        if(checkMSG.getCheckResult().equals("已修改")){
            request.getSession().setAttribute("loginUser", user1);
        }
        request.setAttribute("msg", checkMSG.getCheckResult());
        request.getRequestDispatcher("/pages/mainView/userSetting.jsp").forward(request, response);
    }

    /**
     * 当主页面点击设置的时候用json获取user信息
     * @param req
     * @param resp
     * @throws Exception
     */
    public void getUserMSG(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User user1 = (User)req.getSession().getAttribute("loginUser");
        //使用json传user对象到jsp页面
        Gson gson = new Gson();
        //把user转化为gson形式
        String user = gson.toJson(user1);
        //传入jsp页面
        resp.getWriter().write(user);
    }

    /**
     * 注销登陆
     * @param req
     * @param resp
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //1. 清空session域中内容
        req.getSession().removeAttribute("loginUser");
        //2. 使session失效
        req.getSession().invalidate();
        //3. 清空cookie内容
        Cookie cookie1 = new Cookie("userinfo", "");
        cookie1.setMaxAge(0);
        cookie1.setPath("/");
        cookie1.setHttpOnly(true);
        resp.addCookie(cookie1);
        //提示浏览器已完成
        resp.getWriter().write("1");
    }

    /**
     * 用户选择已了解
     * @param req
     * @param resp
     * @throws Exception
     */
    public void hadRead(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User user1 = (User)req.getSession().getAttribute("loginUser");
        //匹配信息内容
        String content = req.getParameter("content");
        UserImformation userImformation = new UserImformation("state=know");
        CheckMSG checkMSG = new UserMsgController().hadRead(userImformation, content, req);
        if(checkMSG.getCheckResult().equals("已修改")){
            //把userMsg重新放进session域
            UserImformation userImformation1 = new UserImformation("userId=" + user1.getId());
            List<UserImformation> allMessage = new UserAndFriendController().getAllMessage(userImformation1);
            req.getSession().setAttribute("allUserMsgList", allMessage);
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 删除一条消息
     * @param req
     * @param resp
     * @throws Exception
     */
    public void deleteMessage(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User user1 = (User)req.getSession().getAttribute("loginUser");
        String content = req.getParameter("content");
        UserImformation userImformation = new UserImformation("content=" + content, "userId="+ user1.getId());
        CheckMSG checkMSG = new UserMsgController().deleteMessage(userImformation, content);
        if(checkMSG.getCheckResult().equals("已删除")){
            //把userMsg重新放进session域
            UserImformation userImformation1 = new UserImformation("userId=" + user1.getId());
            List<UserImformation> allMessage = new UserAndFriendController().getAllMessage(userImformation1);
            req.getSession().setAttribute("allUserMsgList", allMessage);
            resp.getWriter().write("1");
            return;
        }
    }

    //反馈信息检测账号
    public void checkManagerAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String managerAccount = req.getParameter("managerAccount");
        if(CommonUtil.judgeMSG(managerAccount) == true){
            resp.getWriter().write("0");
            return;
        }
        User user = new User("account=" + managerAccount);
        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        if(user1.size() == 0){
            resp.getWriter().write("1");
            return;
        }else if(!(user1.get(0).getUserKind().equals("create_people") || user1.get(0).getUserKind().equals("manager"))){
            resp.getWriter().write("2");
            return;
        }else{
            resp.getWriter().write("3");
            return;
        }
    }

    //检测输入有没有敏感词
    public void checkTextArea(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String feedbackText = req.getParameter("feedback");
        if(CommonUtil.judgeMSG(feedbackText) == true){
            resp.getWriter().write("2");
            return;
        }
        if(feedbackText.contains("=")){
            resp.getWriter().write("3");
            return;
        }
        int i = new UserMsgController().checkTextArea(feedbackText);
        if(i == 1){
            //证明有敏感词
            resp.getWriter().write("1");
        }else{
            resp.getWriter().write("0");
        }
    }

    public void addMsgInFeedback(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User loginUser = (User)req.getSession().getAttribute("loginUser");
        String msg = req.getParameter("msg");
        String msg1 = req.getParameter("msg1");
        if(CommonUtil.judgeMSG(msg)== true && CommonUtil.judgeMSG(msg1)== true){
        //获取发送给的管理员账号
        String userAccount = req.getParameter("account");
        //查找用户
        User user = new UserLoginAndRegistController().getUser(new User("account=" + userAccount)).get(0);
        //获取反馈信息
        String feedback = req.getParameter("feedback");
        //获取提示信息msg和msg1
            UserImformation userImformation = new UserImformation(0, user.getId(), user.getAccount(), "反馈信息","来自"+loginUser.getName()+":"+feedback, user.getName(), "unknow");
            int i = new UserMsgController().addMsgInFeedback(userImformation);
            if(i == 1){
                resp.getWriter().write("1");
                return;
            }else{
                resp.getWriter().write("0");
                return;
            }
        }else{
            //证明这时错误信息还在
            resp.getWriter().write("error");
            return;
        }
    }
}
