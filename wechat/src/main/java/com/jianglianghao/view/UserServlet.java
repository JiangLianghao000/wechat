package com.jianglianghao.view;

import com.google.gson.Gson;
import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserFriend;
import com.jianglianghao.service.ManagerService;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.MailUtil;
import com.jianglianghao.util.PasswordUtil;
import com.jianglianghao.util.StringUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.jianglianghao.util.CommonUtil.verifyCodeString;
import static com.jianglianghao.util.MailUtil.sendMail;
import static com.jianglianghao.util.storageUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 用户登陆注册和找回密码
 * @verdion
 * @date 2021/4/280:45
 */

public class UserServlet extends com.jianglianghao.view.BaseServlet {

    /**
     * 注册
     *
     * @param req
     * @param resp
     * @return
     */
    public void regist(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String choice = req.getParameter("type");
        //格式都正确，进行邮箱验证，进行数据库添加
        String name = req.getParameter("name");
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        //加密
        String s = PasswordUtil.encryptAES(password);
        String email = req.getParameter("email");
        User user = new User("name=" + name, "account=" + account, "email=" + email, "userKind="+choice, "emailPermission=public", "is_active=0");
        user.setPassword(s);
        //存入数据库中
        CheckMSG checkMSG = new UserLoginAndRegistController().userRegist(user);
        if (checkMSG.getCheckResult().equals("别忘记激活账号")) {
            //发送邮件
            MailUtil.sendMail(user);
            //回显表单数据
            req.setAttribute("name", name);
            req.setAttribute("account", account);
            req.setAttribute("email", email);
            req.setAttribute("msg", "注册成功，别忘记激活账号");
            req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
        } else {
            //回显表单数据
            req.setAttribute("name", name);
            req.setAttribute("account", account);
            req.setAttribute("email", email);
            req.setAttribute("msg", "注册失败，请检查");
            req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
        }
    }

    /**
     * 登陆
     *
     * @param req
     * @param resp
     * @return
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) {
        //获取登陆时间
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(calendar.getTime());
        try {
            //先对user的好友添加进行查找，看有没有好友添加为好友。
            //TODO 设置区分大小写的问题！！！
            String verifycode = req.getParameter("verifycode");
            String account = req.getParameter("useraccount");
            String password = req.getParameter("password");
            //获取是否自动登陆
            String auto = req.getParameter("auto");
            if(auto != null){
                //选了自动登陆
                //创建cookie
                String userinfo = account + "#" + password;
                Cookie cookie = new Cookie("userinfo", PasswordUtil.encode(userinfo));
                //设置7天内自动登陆
                cookie.setMaxAge(60*60*24*14);
                //可在同一应用服务器内共享
                cookie.setPath("/");
                //会增加对xss防护的安全系数
                cookie.setHttpOnly(true);
                resp.addCookie(cookie);
            }
            User user = new User("account=" + account, "password=" + password);
            //对密码加密处理，和数据库中的密码进行匹配
            String encrpyPassword = PasswordUtil.encryptAES(password);
            //调用方法查找
            user.setAccount(account);
            user.setPassword(encrpyPassword);
            //调用方法进行判断
            CheckMSG checkMSG = new UserLoginAndRegistController().userLogin(user, req);
            //获取session对象
            //对验证码判断
            if (verifycode == null || verifycode.trim().length() == 0 || verifyCodeString.compareToIgnoreCase(verifycode) != 0) {
                // 把错误信息，和回显的表单项信息，保存到Request域中
                req.setAttribute("msg", "验证码错误或不为空");
                req.setAttribute("useraccount", account);
                //跳回登录页面
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
                return;
            }
            if(checkMSG.getCheckResult().equals("travel")){
                //把错误信息，和回显的表单项信息，保存到Request域中
                req.setAttribute("msg", "你是游客，请到指定链接登陆");
                req.setAttribute("useraccount", account);
                //跳回登录页面
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
                return;
            }
            if (checkMSG.getCheckResult().equals("is_null")) {
                //把错误信息，和回显的表单项信息，保存到Request域中
                req.setAttribute("msg", "不能输入空的信息");
                req.setAttribute("useraccount", account);
                //   跳回登录页面
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
                return;
            }

            if (checkMSG.getCheckResult().equals("no_exist")) {
                // 把错误信息，和回显的表单项信息，保存到Request域中
                req.setAttribute("msg", "没有找到该用户");
                req.setAttribute("useraccount", account);
                //   跳回登录页面
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
                return;
            }
            if(checkMSG.getCheckResult().equals("beBanned")){
                req.setAttribute("msg", "账号已被封禁");
                req.setAttribute("useraccount", account);
                //   跳回登录页面
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
                return;
            }
            if (checkMSG.getCheckResult().equals("exist")) {
                User loginUser = (User)req.getSession().getAttribute("loginUser");
                //存在该账号，可以登陆，首先查询看看是否有别的好友添加的提示什么的
                //这个friedId表示有人请求添加为好友，wait_for_add表示等待同意添加
                UserFriend userFriend = new UserFriend("friendId=" + loginUser.getId(), "state=wait_for_add" );
                List<UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriend);
                //存入session中，方便各个页面调用
                req.getSession().setAttribute("addFriendIFMT", allFriend);
                //往session中存对象数据
                //存入时间
                req.getSession().setAttribute("loginTime", time);
                //登陆成功跳转到主页面
                resp.sendRedirect(req.getContextPath() + "/pages/mainView/userMain.jsp");
                return;
            }

        } catch (Exception e) {
            req.setAttribute("msg", "登陆失败");
            e.printStackTrace();
        }

    }

    /**
     * 验证码
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置浏览器不缓存本页
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        //生成验证码，写入用户session
        String verifyCode = com.jianglianghao.view.VerifyCode.generateTextCode(com.jianglianghao.view.VerifyCode.TYPE_NUM_UPPER, 4, "0oOilJI1");
        verifyCodeString = verifyCode;
        request.getSession().setAttribute(com.jianglianghao.view.VerifyCode.VERIFY_TYPE_COMMENT, verifyCode);
        //输出验证码给客户端
        response.setContentType("image/jpeg");
				/*
				    textCode 文本验证码
					width 图片宽度
					height 图片高度
					interLine 图片中干扰线的条数
					randomLocation 每个字符的高低位置是否随机
					backColor 图片颜色，若为null，则采用随机颜色
					foreColor 字体颜色，若为null，则采用随机颜色
					lineColor 干扰线颜色，若为null，则采用随机颜色
				*/
        BufferedImage bim = com.jianglianghao.view.VerifyCode.generateImageCode(verifyCode, 70, 22, 15, true, Color.WHITE, Color.BLACK, null);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bim, "JPEG", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    /**
     * 用Ajax对用户名进行检测
     *
     * @param request  请求
     * @param response 请求
     * @return null
     * @throws Exception
     */
    public String checkName(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取值
        String name = request.getParameter("name");
        //进行判断
        User user = new User("name=" + name);
        CheckMSG checkMSG = new UserLoginAndRegistController().checkUserMSG(user);
        if (checkMSG.getCheckResult().equals("exist")) {
            response.getWriter().write("1");
        }
        if (checkMSG.getCheckResult().equals("no_exist")) {
            response.getWriter().write("0");
        }
        return null;
    }

    /**
     * 用Ajax对用户账号进行检测
     *
     * @param request  请求
     * @param response 请求
     * @return null
     * @throws Exception
     */
    public String checkAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = request.getParameter("account");
        //进行判断
        User user = new User("account=" + account);
        if (account == null || account.trim().length() == 0) {
            response.getWriter().write("null");
        } else {
            //正则表达式
            //8-16个字符，至少1个大写字母，1个小写字母，1个数字和1个特殊字符：
            String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}";
            if (account.matches(regex) == true) {
                //正则表达式测试成功，进行处理
                CheckMSG checkMSG = new UserLoginAndRegistController().checkUserMSG(user);
                response.getWriter().write(checkMSG.getCheckResult());
            } else {
                response.getWriter().write("format_error");
            }

        }
        return null;
    }

    public void checkUserPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}";
        String password = request.getParameter("password");
        if (password == null || password.trim().length() == 0 || password.equals("")) {
            response.getWriter().write("null");
            return;
        }
        if (password.matches(regex)) {
            //正则表达式正确
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }
    }

    /**
     * 对邮箱进行账号的验证
     *
     * @param request  请求
     * @param response 回应
     * @return
     * @throws Exception
     */
    public void checkEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = (String) request.getParameter("account");
        String email = request.getParameter("email");
        if (CommonUtil.judgeMSG(account) == true || CommonUtil.judgeMSG(email) == true) {
            //为null
            response.getWriter().write("null");
        }
        User user = new User("account=" + account,"email=" + email);
        CheckMSG checkMSG = new UserLoginAndRegistController().checkUserMSG(user);
        if (checkMSG.getCheckResult().equals("exist")) {
            //找到了邮箱
            response.getWriter().write("exist");
        } else {
            response.getWriter().write("no_exist");
        }
    }

    /**
     * 激活
     * 1为激活状态，-1为注销状态，2为冻结状态
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String account = request.getParameter("account");
        String decode = PasswordUtil.decode(account);
        User user = new User("isActive=1", "account=" + decode);
        //修改active为1
        int result = new UserLoginAndRegistController().modifyUserMSG(user);
        if (result == 1) {
            request.setAttribute("msg", "已激活成功！！！");
            request.getRequestDispatcher("/pages/user/regist.jsp").forward(request, response);
            return;
        } else {
            request.setAttribute("msg", "激活失败！！！");
            request.getRequestDispatcher("/pages/user/regist.jsp").forward(request, response);
            return;
        }
    }

    public void findPassword(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //获取验证码
        String verifycode = request.getParameter("verifycode");
        if(verifycode== null || verifyCodeString.compareToIgnoreCase(verifycode) != 0){
            //验证码不正确
            request.setAttribute("msg", "验证码不正确");
            request.getRequestDispatcher("/pages/user/find_password.jsp").forward(request, response);
            return;
        }
        String email = request.getParameter("email");
        String account = request.getParameter("account");
        User user = new User("account=" + account, "email=" + email);
        //通过account获取user
        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        //找到了，不为0
        if(user1.size() != 0){
            String password = PasswordUtil.decryptAES(user1.get(0).getPassword(), PasswordUtil.key, PasswordUtil.transformation, PasswordUtil.algorithm);
            String content = user1.get(0).getName() + ", 你好！ 你的MyWeChat的密码是: " + password;
            MailUtil.sendMail1(user1.get(0), content, "MyWeChat密码找回提醒");
            request.setAttribute("msg", "已发送邮件");
            request.getRequestDispatcher("/pages/user/find_password.jsp").forward(request, response);
        }else{
            request.setAttribute("msg", "发送邮件失败，请检查输入");
            request.getRequestDispatcher("/pages/user/find_password.jsp").forward(request, response);
        }
    }

    /**
     * 管理员登陆后台的请求
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void managerLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //获取登陆时间
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(calendar.getTime());

        //先对user的好友添加进行查找，看有没有好友添加为好友。
        //TODO 设置区分大小写的问题！！！
        String verifycode = req.getParameter("verifycode");
        String account = req.getParameter("useraccount");
        String password = req.getParameter("password");

        //对验证码进行处理
        if (verifycode == null || verifycode.trim().length() == 0 || managerLoginCode.compareToIgnoreCase(verifycode) != 0) {
            // 把错误信息，和回显的表单项信息，保存到Request域中
            req.setAttribute("msg1", "验证码错误或不为空");
            req.setAttribute("useraccount", account);
            //跳回登录页面
            req.getRequestDispatcher("/pages/user/managerLogin.jsp").forward(req, resp);
            return;
        }

        //获取user，判断是否是管理员
        User user = new User("account=" + account, "password=" + password);
        //对密码加密处理，和数据库中的密码进行匹配
        String encrpyPassword = PasswordUtil.encryptAES(password);
        //调用方法查找
        user.setAccount(account);
        user.setPassword(encrpyPassword);

        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        if(user1.size() == 0){
            //没有找到用户
            req.setAttribute("msg1", "账号或者密码输入错误");
            req.setAttribute("useraccount", account);
            //跳回登录页面
            req.getRequestDispatcher("/pages/user/managerLogin.jsp").forward(req, resp);
            return;
        }else{
            if(!(user1.get(0).getUserKind().equals("manager") || user1.get(0).getUserKind().equals("create_people"))){
                //是普通用户或者游客
                //没有找到用户
                req.setAttribute("msg1", "你没有权限登陆后台管理界面");
                req.setAttribute("useraccount", account);
                //跳回登录页面
                req.getRequestDispatcher("/pages/user/managerLogin.jsp").forward(req, resp);
                return;
            }else{
                //登陆界面，有封号和删除朋友圈两个功能
                //存储管理员
                req.getSession().setAttribute("loginManager",  user1.get(0));
                req.getSession().setAttribute("managerLoginTime",  time);
                //调用方法查找所有用户信息存储起来进行操作
                List<User> allUser = new ManagerService().getAllUser();
                req.setAttribute("ManagerViewfindAllUser", allUser);
                req.getRequestDispatcher("/pages/mainView/managerMainView.jsp").forward(req,resp);
                return;
            }
        }

    }


    /**
     * 管理员登陆界面验证码
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String managerCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置浏览器不缓存本页
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        //生成验证码，写入用户session
        String verifyCode = com.jianglianghao.view.VerifyCode.generateTextCode(com.jianglianghao.view.VerifyCode.TYPE_NUM_UPPER, 4, "0oOilJI1");
        managerLoginCode = verifyCode;
        request.getSession().setAttribute(com.jianglianghao.view.VerifyCode.VERIFY_TYPE_COMMENT, verifyCode);
        //输出验证码给客户端
        response.setContentType("image/jpeg");
				/*
				    textCode 文本验证码
					width 图片宽度
					height 图片高度
					interLine 图片中干扰线的条数
					randomLocation 每个字符的高低位置是否随机
					backColor 图片颜色，若为null，则采用随机颜色
					foreColor 字体颜色，若为null，则采用随机颜色
					lineColor 干扰线颜色，若为null，则采用随机颜色
				*/
        BufferedImage bim = com.jianglianghao.view.VerifyCode.generateImageCode(verifyCode, 70, 22, 15, true, Color.WHITE, Color.BLACK, null);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bim, "JPEG", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }



    /**
     * 管理员登陆界面验证码
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String superManagerCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置浏览器不缓存本页
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        //生成验证码，写入用户session
        String verifyCode = com.jianglianghao.view.VerifyCode.generateTextCode(com.jianglianghao.view.VerifyCode.TYPE_NUM_UPPER, 4, "0oOilJI1");
        superManagerLoginCode = verifyCode;
        request.getSession().setAttribute(com.jianglianghao.view.VerifyCode.VERIFY_TYPE_COMMENT, verifyCode);
        //输出验证码给客户端
        response.setContentType("image/jpeg");
				/*
				    textCode 文本验证码
					width 图片宽度
					height 图片高度
					interLine 图片中干扰线的条数
					randomLocation 每个字符的高低位置是否随机
					backColor 图片颜色，若为null，则采用随机颜色
					foreColor 字体颜色，若为null，则采用随机颜色
					lineColor 干扰线颜色，若为null，则采用随机颜色
				*/
        BufferedImage bim = com.jianglianghao.view.VerifyCode.generateImageCode(verifyCode, 70, 22, 15, true, Color.WHITE, Color.BLACK, null);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bim, "JPEG", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    /**
     * 超管登陆界面，超管只有一个功能，就是可以设置任意人为管理员和解除任意人的管理员身份，相当于一个封号机器
     * @param req
     * @param resp
     * @throws Exception 异常
     */
    public void superManagerLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //获取登陆时间
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(calendar.getTime());

        //先对user的好友添加进行查找，看有没有好友添加为好友。
        //TODO 设置区分大小写的问题！！！
        String verifycode = req.getParameter("verifycode");
        String account = req.getParameter("useraccount");
        String password = req.getParameter("password");

        //对验证码进行处理
        if (verifycode == null || verifycode.trim().length() == 0 || superManagerLoginCode.compareToIgnoreCase(verifycode) != 0) {
            // 把错误信息，和回显的表单项信息，保存到Request域中
            req.setAttribute("msg1", "验证码错误或不为空");
            req.setAttribute("useraccount", account);
            //跳回登录页面
            req.getRequestDispatcher("/pages/user/managerLogin.jsp").forward(req, resp);
            return;
        }

        //获取user，判断是否是管理员
        User user = new User("account=" + account, "password=" + password);
        //对密码加密处理，和数据库中的密码进行匹配
        String encrpyPassword = PasswordUtil.encryptAES(password);
        //调用方法查找
        user.setAccount(account);
        user.setPassword(encrpyPassword);

        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        if(user1.size() == 0){
            //没有找到用户
            req.setAttribute("msg2", "账号或者密码输入错误");
            req.setAttribute("useraccount", account);
            //跳回登录页面
            req.getRequestDispatcher("/pages/user/suuper_manager_login.jsp").forward(req, resp);
            return;
        }else{
            if(!user1.get(0).getUserKind().equals("create_people")){
                //是普通用户或者游客或者管理员
                //没有找到用户
                req.setAttribute("msg2", "你没有权限登陆超管界面");
                req.setAttribute("useraccount", account);
                //跳回登录页面
                req.getRequestDispatcher("/pages/user/suuper_manager_login.jsp").forward(req, resp);
                return;
            }else{
                //界面，可以解除管理员权限和设置管理员
                req.getSession().setAttribute("loginSuperManager", user1.get(0));
                //调用方法查找所有用户信息存储起来进行操作
                List<User> allUser = new ManagerService().getAllUser();
                req.setAttribute("SuperManagerViewfindAllUser", allUser);
                req.getSession().setAttribute("superManagerLoginTime", time);
                req.getRequestDispatcher("/pages/mainView/super_manager_main_view.jsp").forward(req,resp);
                return;
            }
        }
    }

    /**
     * 游客登陆
     * @param req  请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void travelLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        //获取登陆时间
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(calendar.getTime());

        //先对user的好友添加进行查找，看有没有好友添加为好友。
        //TODO 设置区分大小写的问题！！！
        String verifycode = req.getParameter("verifycode");
        String account = req.getParameter("useraccount");
        String password = req.getParameter("password");

        //对验证码进行处理
        if (verifycode == null || verifycode.trim().length() == 0 || travelLoginCode.compareToIgnoreCase(verifycode) != 0) {
            // 把错误信息，和回显的表单项信息，保存到Request域中
            req.setAttribute("msg1", "验证码错误或不为空");
            req.setAttribute("useraccount", account);
            //跳回登录页面
            req.getRequestDispatcher("/pages/user/travelLogin.jsp").forward(req, resp);
            return;
        }

        //获取user，判断是否是管理员
        User user = new User("account=" + account, "password=" + password);
        //对密码加密处理，和数据库中的密码进行匹配
        String encrpyPassword = PasswordUtil.encryptAES(password);
        //调用方法查找
        user.setAccount(account);
        user.setPassword(encrpyPassword);

        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        if(user1.size() == 0){
            //没有找到用户
            req.setAttribute("msg2", "账号或者密码输入错误");
            req.setAttribute("useraccount", account);
            //跳回登录页面
            req.getRequestDispatcher("/pages/user/travelLogin.jsp").forward(req, resp);
            return;
        }else{
            if(!user1.get(0).getUserKind().equals("travel")){
                //是普通用户或者游客或者管理员
                //没有找到用户
                req.setAttribute("msg2", "你不是游客");
                req.setAttribute("useraccount", account);
                //跳回登录页面
                req.getRequestDispatcher("/pages/user/travelLogin.jsp").forward(req, resp);
                return;
            }else{
                req.getSession().setAttribute("loginUser", user1.get(0));
                req.getSession().setAttribute("travelLoginTime", time);
                req.getRequestDispatcher("/pages/mainView/travelMain.jsp").forward(req,resp);
                return;
            }
        }
    }

    /**
     * 查找所有游客的好友
     * @param req
     * @param resp
     * @throws Exception
     */
    public void getTravelFriends(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User user = (User)req.getSession().getAttribute("loginUser");
        String name = user.getName();
        UserFriend userFriend = new UserFriend("friendId="+user.getId(),"isBlacklist=no", "state=friend");
        List<UserFriend> userFriends = new UserAndFriendController().getUserFriends(userFriend);
        if(userFriends.size()!=0){
            Gson gson = new Gson();
            String s = gson.toJson(userFriend);
            resp.getWriter().write(s);
            return;
        }else{
            resp.getWriter().write("0");
            return;
        }
    }


    /**
     * 游客登陆界面验证码
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String travelCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置浏览器不缓存本页
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        //生成验证码，写入用户session
        String verifyCode = com.jianglianghao.view.VerifyCode.generateTextCode(com.jianglianghao.view.VerifyCode.TYPE_NUM_UPPER, 4, "0oOilJI1");
        travelLoginCode = verifyCode;
        request.getSession().setAttribute(com.jianglianghao.view.VerifyCode.VERIFY_TYPE_COMMENT, verifyCode);
        //输出验证码给客户端
        response.setContentType("image/jpeg");
				/*
				    textCode 文本验证码
					width 图片宽度
					height 图片高度
					interLine 图片中干扰线的条数
					randomLocation 每个字符的高低位置是否随机
					backColor 图片颜色，若为null，则采用随机颜色
					foreColor 字体颜色，若为null，则采用随机颜色
					lineColor 干扰线颜色，若为null，则采用随机颜色
				*/
        BufferedImage bim = com.jianglianghao.view.VerifyCode.generateImageCode(verifyCode, 70, 22, 15, true, Color.WHITE, Color.BLACK, null);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bim, "JPEG", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }
}
