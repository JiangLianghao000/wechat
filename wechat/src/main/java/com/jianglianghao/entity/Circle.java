package com.jianglianghao.entity;

import java.lang.reflect.Method;
import java.util.Date;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/1220:19
 */

public class Circle {
    private int contentId;
    private String userName;
    private String userAccount;
    private String content;
    private String contentPermission;
    private String time;
    private String location;

    public Circle() {
    }


    /**
     * 特殊构造器
     * @param s 参数
     */
    public Circle(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("contentId")){
                this.contentId = Integer.parseInt(split[1].trim());
                continue;
            }
            //通过反射获取该属性的set方法
            String setMethod = toSet(split[0]);
            //获取该类的所有方法
            Method[] methods = this.getClass().getMethods();
            for(Method method : methods){
                //判断方法名是否相同
                if(setMethod .equals(method.getName())){
                    method.invoke(this, split[1].trim());
                }
            }
        }
    }

    public Circle(int contentId, String userName, String userAccount, String content, String contentPermission, String time, String location) {
        this.contentId = contentId;
        this.userName = userName;
        this.userAccount = userAccount;
        this.content = content;
        this.contentPermission = contentPermission;
        this.time = time;
        this.location = location;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentPermission() {
        return contentPermission;
    }

    public void setContentPermission(String contentPermission) {
        this.contentPermission = contentPermission;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "contentId=" + contentId +
                ", userName='" + userName + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", content='" + content + '\'' +
                ", contentPermission='" + contentPermission + '\'' +
                ", time='" + time + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
