package com.jianglianghao.entity;

import java.lang.reflect.Method;

import static com.jianglianghao.util.StringUtil.toSet;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 群文件建
 * @verdion
 * @date 2021/5/618:43
 */

public class GroupFile {
    private int id;
    private String groupAccount;
    //群文件类型
    private String fileType;
    //文件路径
    private String fileLocation;
    private String userAccount;
    private String userName;
    //群id


    public GroupFile() {
    }

    @Override
    public String toString() {
        return "groupFile{" +
                "id=" + id +
                ", groupAccount='" + groupAccount + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public GroupFile(int id, String groupAccount, String fileType, String fileLocation, String userAccount, String userName) {
        this.id = id;
        this.groupAccount = groupAccount;
        this.fileType = fileType;
        this.fileLocation = fileLocation;
        this.userAccount = userAccount;
        this.userName = userName;
    }

    public GroupFile(Object... s) throws Exception {
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

    public String getGroupAccount() {
        return groupAccount;
    }

    public void setGroupAccount(String groupAccount) {
        this.groupAccount = groupAccount;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
