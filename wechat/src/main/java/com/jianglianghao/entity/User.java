package com.jianglianghao.entity;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static com.jianglianghao.util.StringUtil.getBriefMethodByName;
import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/261:14
 */

public class User {
    private int id;
    private String name;
    private String account;
    private String password;
    private String email;
    private String emailPermission;
    private String userKind;
    private String headProtrait;
    private String isActive;

    public User() {
    }




    /**
     * 特殊构造器
     * @param s 参数
     */
    public User(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("id")){
                this.id = Integer.parseInt(split[1].trim());
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

    public User(int id, String name, String account, String password, String email, String emailPermission, String userKind, String headProtrait,  String isActive) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
        this.email = email;
        this.emailPermission = emailPermission;
        this.userKind = userKind;
        this.headProtrait = headProtrait;
        this.isActive = isActive;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPermission() {
        return emailPermission;
    }

    public void setEmailPermission(String emailPermission) {
        this.emailPermission = emailPermission;
    }

    public String getUserKind() {
        return userKind;
    }

    public void setUserKind(String userKind) {
        this.userKind = userKind;
    }

    public String getHeadProtrait() {
        return headProtrait;
    }

    public void setHeadProtrait(String headProtrait) {
        this.headProtrait = headProtrait;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", emailPermission='" + emailPermission + '\'' +
                ", userKind='" + userKind + '\'' +
                ", headProtrait='" + headProtrait + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
