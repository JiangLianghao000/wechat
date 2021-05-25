package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/723:00
 */

public class UserImformation {
    private int id;
    private int userId;
    private String peopleAccount;
    private String type;
    private String content;
    private String peopleName;
    private String state;

    public UserImformation(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("userId")){
                this.userId = Integer.parseInt(split[1].trim());
                continue;
            }
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
    
    public UserImformation() {
    }

    @Override
    public String toString() {
        return "UserImformation{" +
                "id=" + id +
                ", userId=" + userId +
                ", peopleAccount='" + peopleAccount + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", peopleName='" + peopleName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public UserImformation(int id, int userId, String peopleAccount, String type, String content, String peopleName, String state) {
        this.id = id;
        this.userId = userId;
        this.peopleAccount = peopleAccount;
        this.type = type;
        this.content = content;
        this.peopleName = peopleName;
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPeopleAccount() {
        return peopleAccount;
    }

    public void setPeopleAccount(String peopleAccount) {
        this.peopleAccount = peopleAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
