package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/618:43
 */

public class Groups {
    private int id;
    //用户id
    private int userId;
    //群聊账号
    private String groupAccount;
    //群聊名字
    private String groupName;
    //群聊头像
    private String groupHeadportrait;
    //群聊公告
    private String groupAnnounce;




    /**
     * 特殊构造器
     * @param s 参数
     */
    public Groups(Object... s) throws Exception {
        for (int i = 0; i < s.length; i++) {
            //分成两部分左边是属性，右边是属性值
            String[] split = String.valueOf(s[i]).split("=");
            //对int单独处理
            if(split[0].trim().equals("id")){
                this.id = Integer.parseInt(split[1].trim());
                continue;
            }

            if(split[0].trim().equals("userId")){
                this.userId = Integer.parseInt(split[1].trim());
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

    @Override
    public String toString() {
        return "Groups{" +
                "id=" + id +
                ", userId=" + userId +
                ", groupAccount='" + groupAccount + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupHeadportrait='" + groupHeadportrait + '\'' +
                ", groupAnnounce='" + groupAnnounce + '\'' +
                '}';
    }

    public Groups(int id, int userId, String groupAccount, String groupName, String groupHeadportrait, String groupAnnounce) {
        this.id = id;
        this.userId = userId;
        this.groupAccount = groupAccount;
        this.groupName = groupName;
        this.groupHeadportrait = groupHeadportrait;
        this.groupAnnounce = groupAnnounce;
    }

    public Groups() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGroupAccount() {
        return groupAccount;
    }

    public void setGroupAccount(String groupAccount) {
        this.groupAccount = groupAccount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupHeadportrait() {
        return groupHeadportrait;
    }

    public void setGroupHeadportrait(String groupHeadportrait) {
        this.groupHeadportrait = groupHeadportrait;
    }

    public String getGroupAnnounce() {
        return groupAnnounce;
    }

    public void setGroupAnnounce(String groupAnnounce) {
        this.groupAnnounce = groupAnnounce;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
