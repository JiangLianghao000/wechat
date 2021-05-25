package com.jianglianghao.view.filter;

import com.jianglianghao.controller.GroupChatController;
import com.jianglianghao.controller.ManagerController;
import com.jianglianghao.controller.UserAndFriendController;
import com.jianglianghao.controller.UserLoginAndRegistController;
import com.jianglianghao.entity.Ban;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserFriend;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.PasswordUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/511:14
 */

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    /**
     * 自动登陆拦截
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        //查看session域中是否有用户信息，有就代表了已经登陆，不拦截
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            //判断cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                //证明选了自动登陆，判断cookie是否正确
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("userinfo")) {
                        //已记录登陆的cookies
                        //获取cookies的内容
                        String value = cookie.getValue();
                        //对value解密
                        String decode = PasswordUtil.decode(value);
                        String[] userinfos = decode.split("#");
                        //获取user
                        try {
                            String password1 = PasswordUtil.encryptAES(userinfos[1]);
                            User user = new User("account=" + userinfos[0]);
                            user.setPassword(password1);
                            //调用方法获取user
                            List<User> user1 = new UserLoginAndRegistController().getUser(user);


                            //判断用户是否已经被封号了
                            Ban ban = new Ban("beBannedPeople="+user1.get(0).getName());
                            List<Ban> bans = new ManagerController().finaBan(ban);
                            if(bans.size() != 0){
                                //证明被封号了，清除cookies
                                Cookie cookie1 = new Cookie("userinfo", "");
                                cookie1.setMaxAge(0);
                                cookie1.setPath("/");
                                response.addCookie(cookie1);
                                servletRequest.getRequestDispatcher("/pages/user/login.jsp").forward(servletRequest,servletResponse);
                                return;
                            }


                            user1.get(0).setPassword(PasswordUtil.decryptAES(user1.get(0).getPassword(), PasswordUtil.key, PasswordUtil.transformation, PasswordUtil.algorithm));
                            //找到的user1有user而且是已经激活了而且不是游客,证明这时候输入的账号密码是匹配的
                            if (user1.size() != 0 && user1.get(0).getIsActive().equals("1") && !user1.get(0).getUserKind().equals("tourist")) {
                                //获取登陆时间
                                Calendar calendar = Calendar.getInstance();
                                Date date = new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time = formatter.format(calendar.getTime());
                                //存入session
                                request.getSession().setAttribute("loginTime", time);
                                
                                request.getSession().setAttribute("loginUser", user1.get(0));
                                filterChain.doFilter(servletRequest,servletResponse);

                                //获取可能有人请求添加为好友的信息
                                UserFriend userFriend = new UserFriend("friendId=" + user1.get(0).getId(), "state=wait_for_add" );
                                List<UserFriend> allFriend = new UserAndFriendController().getAllFriend(userFriend);
                                //存入session中，方便各个页面调用
                                request.getSession().setAttribute("addFriendIFMT", allFriend);

                                response.sendRedirect(request.getContextPath() + "/pages/mainView/userMain.jsp");
                                return;
                            } else {
                                //没有找到账号密码对应的用户，证明这时候cookies中的内容和账号密码不匹配，用户可能修改密码了
                                //删除cookies
                                Cookie cookie1 = new Cookie("userinfo", "");
                                cookie1.setMaxAge(0);
                                cookie1.setPath("/");
                                response.addCookie(cookie1);
                                servletRequest.getRequestDispatcher("/pages/user/login.jsp").forward(servletRequest,servletResponse);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                //遍历到这里，证明没有匹配的cookie
                //session域中没有用户信息而且用户也没有选自动登陆
                //跳转到登陆界面
                request.getRequestDispatcher("/pages/user/login.jsp").forward(request,response);
                return;
            }
        } else {
            // 让程序继续往下访问用户的目标资源
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
    }


}
