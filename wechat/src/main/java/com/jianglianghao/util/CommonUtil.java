package com.jianglianghao.util;

import com.google.gson.Gson;
import com.jianglianghao.bean.GroupMsg;
import com.jianglianghao.bean.ResultMessage;
import com.jianglianghao.view.VerifyCode;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import sun.misc.BASE64Encoder;
import sun.nio.cs.ext.MS874;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/250:50
 */

public class CommonUtil {
    //提纯html文本
    public static String getText(String Html) {
        String text = Html.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        text = text.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
        return text;
    }

    /**
     * @param MSG 要判断的信息
     * @return
     * @Description 没有数据或者null返回true
     */
    public static boolean judgeMSG(String MSG) {
        if (MSG == null || MSG.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * @Description 判断两个对象值是否相同
     * @param o1 第一个对象
     * @param o2 第二个对象
     * @return
     */
    public static boolean judgeEqual(Object o1, Object o2){
        if(o1.equals(o2) == true){
            return true;
        }else{
            return false;
        }
    }

    //用于保存每次的验证码
    public static String verifyCodeString = null;

    /**
     * 查找指定的名词的cookies
     * @param name 指定名字
     * @param cookies cookies
     * @return
     */
    public static Cookie findCookies(String name, Cookie[] cookies){
        if(name == null || cookies == null || cookies.length == 0){
            return null;
        }
        for (Cookie cookie : cookies) {
            if(name.equals(cookie.getName())){
                //找到了
                return cookie;
            }
        }
        //没找到
        return null;
    }

    //去除标签
    /**
     * 去除字符串中html标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHTMLTag(String htmlStr) {
        if (htmlStr == null || "".equals(htmlStr)) {
            return "";
        }
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        // 过滤script标签
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        // 过滤style标签
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        // 过滤html标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        return htmlStr;
    }

    /**
     * 消息工具类，把消息转化为json字符串返回给页面
     * @param isSystemMessage 是否是系统消息
     * @param fromName 推送给谁
     * @param message
     * @return
     */
    public static String getMessage(boolean isSystemMessage, String fromName, Object message, String type, String head){
        try {
            ResultMessage result = new ResultMessage();
            result.setIsSystem(isSystemMessage);
            result.setMessage(message);
            //如果是系统消息，就是null，就不设置了
            if(fromName != null){
                result.setFromName(fromName);
            }
            if(type != null){
                result.setMessageType(type);
            }
            result.setHead(head);
            //转化为json字符串返回
            Gson gson = new Gson();
            return gson.toJson(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用base64解码
     * @param filename 文件名
     * @return 解码后的名字
     */
    public static String Base64EcondingFileName(String filename){
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            return "=?UTF-8?B?" + new String(base64Encoder.encode(filename.getBytes("UTF-8"))) + "?=";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param fromName
     * @param message
     * @param type
     * @param groupName
     * @param head
     * @return
     */
    public static  String getGroupMsg(boolean isSystemMessage,String fromName, Object message, String type, String groupName, String head){
        GroupMsg groupMsg = new GroupMsg();
        //设置是不是系统消息
        groupMsg.setIsSystem(isSystemMessage);
        //设置发送给哪个群
        groupMsg.setGroupName(groupName);
        //设置发送的消息来自谁
        groupMsg.setFromName(fromName);
        //设置头像
        groupMsg.setHead(head);
        //设置发送的消息的类型
        groupMsg.setMessageType(type);
        //设置发送的消息
        groupMsg.setMessage(message);
        Gson gson = new Gson();
        return gson.toJson(groupMsg);
    }
}
