package com.jianglianghao.view;
import com.jianglianghao.bean.DelayBan;
import com.jianglianghao.bean.DelayQuenueManager;
import com.jianglianghao.view.BaseServlet;
import com.jianglianghao.controller.CircleController;
import com.jianglianghao.controller.ManagerController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.controller.UserMsgController;
import com.jianglianghao.entity.Ban;
import com.jianglianghao.entity.Circle;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserImformation;
import com.jianglianghao.service.ManagerService;
import com.jianglianghao.util.storageUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.jianglianghao.util.storageUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1521:22
 */

public class ManagerServlet extends BaseServlet {

    /**
     * 管理员界面查找用户
     *
     * @param req  请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void findUserInManagerView(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String time = null;
        String account = req.getParameter("account");
        List<User> user = new UserLoginAndRegistController().getUser(new User("account=" + account));
        if (user.size() != 0) {
            req.getSession().setAttribute("managerViewFindUser", user.get(0));
            resp.getWriter().write("1");
            return;
        } else {
            resp.getWriter().write("0");
            return;
        }
    }

    /**
     * 通过账号查找用户
     *
     * @param req  请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void findUserByAccountInManagerView(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String account = req.getParameter("account");
        List<User> user = new UserLoginAndRegistController().getUser(new User("account=" + account));
        if (user.size() == 0) {
            resp.getWriter().write("0");
            return;
        } else {
            //找到了
            req.getSession().setAttribute("managerViewFindUser", user.get(0));
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 封号处理，用cookies存入网站，设置天数
     *
     * @param req
     * @param resp
     * @throws Exception
     */
    public void banUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //获取manager
        User user = (User)req.getSession().getAttribute("loginManager");
        User user1 = (User)req.getSession().getAttribute("managerViewFindUser");
        //检测是不是管理员或者更高级的
        if(user.getUserKind().equals("manager") && (user1.getUserKind().equals("manager") || user1.getUserKind().equals("create_people"))){
            //用户是管理员并且被删除的人是管理员或者create_people，不可封号
            resp.getWriter().write("-1");
            return;
        }
        if(user.getUserKind().equals("create_people") && user1.getUserKind().equals("create_people")){
            //用户是create_people并且被删除的人是create_people，不可封号
            resp.getWriter().write("-1");
            return;
        }
        String days = req.getParameter("day");
        if(days.equals("")){
            return;
        }
        //获取要封号的时间
        int day = Integer.parseInt(req.getParameter("day"));
        //格式化，获取解封的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date afterDate = new Date(now.getTime() + day * 24 * 60 * 60 * 1000);
        String time = sdf.format(afterDate);


        //检测是否已经被封号了
        Ban ban = new Ban(0, user1.getName(), user.getName(), time);
        int i = new ManagerController().bandUser(ban);
        if (i == 0) {
            //被封号了
            resp.getWriter().write("0");
            return;
        } else {
            //没有被封号
            DelayQuenueManager instance = DelayQuenueManager.getInstance();
            //加入延迟队列
            instance.add(new DelayBan(user1.getName(), user.getName(), null, time));
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 取消封号
     *
     * @param req  请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void unBandUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user1 = (User)req.getSession().getAttribute("managerViewFindUser");
        User user = (User)req.getSession().getAttribute("loginManager");
        //从数据库中去除封号的信息
        //获取名字
        String name = user1.getName();
        if (user.getName().equals(user1.getName())) {
            //自己不能封自己号
            resp.getWriter().write("-2");
            return;
        }
        if (user.getUserKind().equals("manager") && user1.getUserKind().equals("manager")) {
            //两个都是管理员
            resp.getWriter().write("-1");
            return;
        }
        Ban ban = new Ban("beBannedPeople=" + name);

        int i = new ManagerController().unBandUser(ban);
        if (i == 0) {
            resp.getWriter().write("0");
            return;
        }
        if (i == 1) {
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * manager界面通过账号查找用户朋友圈
     *
     * @param req  请求
     * @param resp 转发
     * @throws Exception 异常
     */
    public void findUserCircleByManagerPrintAccount(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String account = req.getParameter("account");
        User user = new User("account=" + account);
        List<User> user1 = new UserLoginAndRegistController().getUser(user);
        if (user1.size() == 0) {
            resp.getWriter().write("0");
            return;
        } else {
            //获取查找的名字
            String name = user1.get(0).getName();
            //通过该名字找到属于该账号的所有的朋友圈
            List<Circle> circle = new CircleController().getCircle(new Circle("userName=" + name));
            //存入域中
            req.getSession().setAttribute("managerFindCicle", circle);
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 根据id找内容
     *
     * @param req  请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void findContent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String contentId = req.getParameter("id");
        Circle circle = new Circle("contentId=" + contentId);
        List<Circle> circle1 = new CircleController().getCircle(circle);
        //存储找到的朋友圈动态
        req.getSession().setAttribute("managerViewFindCircleById", circle1.get(0));
        resp.getWriter().write(circle1.get(0).getContent());
        return;
    }

    /**
     * 管理员删除朋友圈
     *
     * @param req  请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void deleteCircle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Circle circle1 = (Circle)req.getSession().getAttribute("managerViewFindCircleById");
        //不能删除同级别或更高级别的人
        User user1 = (User)req.getSession().getAttribute("loginManager");
        String userName1 = circle1.getUserName();
        User user2 = new User("name="+userName1);
        List<User> user3 = new UserLoginAndRegistController().getUser(user2);
        if(user3.size() != 0){
            String kind = user3.get(0).getUserKind();
            if(user1.getUserKind().equals("manager") && (kind.equals("manager") || kind.equals("create_people"))){
                resp.getWriter().write("-1");
                return;
            }
            if(user1.getUserKind().equals("create_people") && kind.equals("create_people")){
                resp.getWriter().write("-1");
                return;
            }
        }


        Circle circle = circle1;
        int i = new ManagerService().deleteCircle(circle);
        if (i == 1) {
            //删除了朋友圈，就要更新域的内容了
            String userName = circle1.getUserName();
            List<Circle> circle2 = new CircleController().getCircle(new Circle("userName=" + userName));
            //存入域中
            req.getSession().setAttribute("managerFindCicle", circle2);
            //通知一下被删除人
            //先获取人
            List<User> user = new UserLoginAndRegistController().getUser(new User("name=" + userName));
            UserImformation userImformation = new UserImformation(0, user.get(0).getId(), user.get(0).getAccount(), "反馈信息",
                    "你的编号为"+ circle1.getContentId() +"的朋友圈因某种原因被封禁，如需了解详情，请联系该管理员邮箱:"+user1.getEmail(),
                    user.get(0).getName(), "unkonw");
            new UserMsgController().addMsgInFeedback(userImformation);
            resp.getWriter().write("1");
            return;
        } else {
            resp.getWriter().write("0");
            return;
        }
    }

    /**
     * 在超管界面查找用户
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void findUserInSuperManagerView(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String account = req.getParameter("account");
        List<User> user = new UserLoginAndRegistController().getUser(new User("account=" + account));
        if (user.size() == 0) {
            resp.getWriter().write("0");
            return;
        } else {
            //找到了
            req.getSession().setAttribute("superManagerViewFindUser", user.get(0));
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 在超管界面查询用户信息
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void findUserByAccountInSuperManagerView(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String account = req.getParameter("account");
        List<User> user = new UserLoginAndRegistController().getUser(new User("account=" + account));
        if (user.size() == 0) {
            resp.getWriter().write("0");
            return;
        } else {
            //找到了
            req.getSession().setAttribute("superManagerViewFindUser", user.get(0));
            resp.getWriter().write("1");
            return;
        }
    }

    /**
     * 取消管理员权限
     * @param req 请求
     * @param resp 转发
     * @throws Exception
     */
    public void banManager(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User user2 = (User)req.getSession().getAttribute("loginSuperManager");
        User user1 = (User)req.getSession().getAttribute("superManagerViewFindUser");
        //取消管理员权限
        //首先获取用户
        User superManagerViewFindUser = user1;
        //不可以修改自己的权限
        if(user1.getName().equals(user2.getName())){
            resp.getWriter().write("2");
            return;
        }
        //判断是不是游客
        if(superManagerViewFindUser.getUserKind().equals("travel")){
            //是游客
            resp.getWriter().write("4");
            return;
        }
        //判断是不是管理员
        if(!superManagerViewFindUser.getUserKind().equals("manager")){
            //是超管
            resp.getWriter().write("0");
            return;
        }else{
            User user = new User("userKind=user");
            //是超管,可以删除管理员权限
            int i = new ManagerController().banManager(user, superManagerViewFindUser.getAccount());
            if(i == 1){
                superManagerViewFindUser.setUserKind("user");
                //通知对方
                UserImformation userImformation = new UserImformation(0, superManagerViewFindUser.getId(), superManagerViewFindUser.getAccount(), "反馈信息",
                        "你已被降级为普通用户，如需了解详情，请联系该管理员邮箱:"+user2.getEmail(),
                        superManagerViewFindUser.getName(), "unknow");
                new UserMsgController().addMsgInFeedback(userImformation);
                resp.getWriter().write("1");
                return;
            }
        }
    }

    /**
     * 设置为管理员
     * @param req 请求
     * @param resp 响应
     * @throws Exception 异常
     */
    public void addToManager(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        User user2 = (User)req.getSession().getAttribute("loginSuperManager");
        //首先获取用户
        User user1 = (User)req.getSession().getAttribute("superManagerViewFindUser");
        if(user2.getName().equals(user1.getName())){
            resp.getWriter().write("2");
            return;
        }
        //判断是不是游客
        if(user1.getUserKind().equals("travel")){
            //是游客
            resp.getWriter().write("4");
            return;
        }
        if(user1.getUserKind().equals("manager")){
            //是管理员
            resp.getWriter().write("0");
            return;
        }else{
            //不是管理员，就可以修改权限了
            User user = new User("userKind=manager");
            int i = new ManagerController().banManager(user, user1.getAccount());
            //通知信息
            if(i == 1){
                //重新设置
                user1.setUserKind("manager");
                //通知对方
                UserImformation userImformation = new UserImformation(0, user1.getId(), user1.getAccount(), "反馈信息",
                        "你已被升级为管理员，如需了解详情，请联系该管理员邮箱:"+user2.getEmail(),
                        user1.getName(), "unknow");
                new UserMsgController().addMsgInFeedback(userImformation);
                resp.getWriter().write("1");
                return;
            }
        }
    }
}
