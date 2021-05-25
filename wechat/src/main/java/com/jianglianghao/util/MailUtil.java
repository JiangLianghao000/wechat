package com.jianglianghao.util;

import com.jianglianghao.entity.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.net.Inet4Address;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/210:57
 */

public class MailUtil {

    /**
     * 发送邮件
     * @param user 实体类
     */
    public static void sendMail(User user){
        //自己的账号：发邮件
        String emailAccount = "2429890953@qq.com";
        //授权码
        String myPass = "bzniwgwpkqyvecja";
        //对会话设置
        Properties pros = new Properties();
        //设置邮件发送协议，一般都用smtp：简单邮件发送协议
        pros.setProperty("mail.transport.protocol", "smtp");
        //设置邮箱服务器
        pros.setProperty("mail.host", "smtp.qq.com");
        //设置开启smtp的认证功能，为了确保安全
        pros.setProperty("mail.smtp.auth", "true");
        //开启ssl加密功能
        pros.setProperty("mail.smtp.ssl.enable", "true");
        //设置socket连接工厂
        // pros.setProperty("mail.smtp.ssl.socketFactory", );
        //1. 建立会话
        Session session = Session.getDefaultInstance(pros, new Authenticator() {
            //进行与163/126/qq等服务器之间的认证
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //发件人邮箱账号和授权码，此时的密码不是密码，是授权吗
                return new PasswordAuthentication(emailAccount, myPass);
            }
        });

        session.setDebug(true);
        try {
            //2. 获取一个传输端口
            Transport transport = session.getTransport();
            //3. 此处必须明确连接一下服务器，否则产生not connection异常
            transport.connect(emailAccount, myPass);
            //4. 创建message对象
            MimeMessage message = createMsg(session, emailAccount, user);
            //5. 利用传输端口发邮件
            transport.sendMessage(message, message.getAllRecipients());
            //6. 关闭传输端口
            transport.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //传进msg信息
    //创建邮件
    private static MimeMessage createMsg(Session session, String emailAccount, User user){
        MimeMessage message = new MimeMessage(session);
        try {
            //设置发件人
            message.setFrom(new InternetAddress(emailAccount, "MyWeChat", "UTF-8"));
            //设置接收人
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(user.getEmail(), user.getName(), "UTF-8")});
            //邮件主题
            message.setSubject("MyWeChat激活");
            //邮件内容
            //获取本机的ip地址
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            //点击就跳转到userServlet中的active方法中根据getAccount中的account来激活;
            String url = "http://localhost:8080/wechat/userServlet?method=active&account="+ com.jianglianghao.util.PasswordUtil.encode(user.getAccount());
            System.out.println(url);
            message.setContent(user.getName() + "你好！<br>欢迎注册MyWeChat账号！请点击连接激活账号:<a href='" + url + "'>点击此处</a>", "text/html;charset=utf-8");
            //设置邮件发送时间
            message.setSentDate(new Date());
            //保存设置
            message.saveChanges();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


    /**
     * 发送邮件
     * @param user 实体类
     * @param content 邮件内容
     */
    public static void sendMail1(User user, String content, String subject){
        //自己的账号：发邮件
        String emailAccount = "2429890953@qq.com";
        //授权码
        String myPass = "bzniwgwpkqyvecja";
        //对会话设置
        Properties pros = new Properties();
        //设置邮件发送协议，一般都用smtp：简单邮件发送协议
        pros.setProperty("mail.transport.protocol", "smtp");
        //设置邮箱服务器
        pros.setProperty("mail.host", "smtp.qq.com");
        //设置开启smtp的认证功能，为了确保安全
        pros.setProperty("mail.smtp.auth", "true");
        //开启ssl加密功能
        pros.setProperty("mail.smtp.ssl.enable", "true");
        //设置socket连接工厂
        // pros.setProperty("mail.smtp.ssl.socketFactory", );
        //1. 建立会话
        Session session = Session.getDefaultInstance(pros, new Authenticator() {
            //进行与163/126/qq等服务器之间的认证
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //发件人邮箱账号和授权码，此时的密码不是密码，是授权吗
                return new PasswordAuthentication(emailAccount, myPass);
            }
        });

        session.setDebug(true);
        try {
            //2. 获取一个传输端口
            Transport transport = session.getTransport();
            //3. 此处必须明确连接一下服务器，否则产生not connection异常
            transport.connect(emailAccount, myPass);
            //4. 创建message对象
            MimeMessage message = createMsg1(session, emailAccount, user, content, subject);
            //5. 利用传输端口发邮件
            transport.sendMessage(message, message.getAllRecipients());
            //6. 关闭传输端口
            transport.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param session 会话
     * @param emailAccount 发件人邮箱账号
     * @param user 用户
     * @param content 邮箱内容
     * @return
     */
    //通过参数创建邮件
    public static MimeMessage createMsg1(Session session, String emailAccount, User user,  String content, String subject){
        MimeMessage message = new MimeMessage(session);
        try {
            //设置发件人
            message.setFrom(new InternetAddress(emailAccount, "MyWeChat", "UTF-8"));
            //设置接收人
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(user.getEmail(), user.getName(), "UTF-8")});
            //邮件主题
            message.setSubject(subject);
            //邮件内容
            //获取本机的ip地址
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            //设置邮件内容
            message.setContent(content, "text/html;charset=utf-8");
            //设置邮件发送时间
            message.setSentDate(new Date());
            //保存设置
            message.saveChanges();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
}
