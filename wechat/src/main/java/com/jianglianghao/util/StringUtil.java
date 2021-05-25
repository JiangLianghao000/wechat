package com.jianglianghao.util;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/260:43
 */

public class StringUtil {
    //存储要获取的信息
    public static List<Object> getMSG = new LinkedList<>();
    //拼接字符串
    public static StringBuilder sql = new StringBuilder();

    //数据库字段和entity实体类的转化

    /**
     * @param db 传入的数据
     * @return 返回转化后的数据
     */
    public static String dbNameReverseToFieldName(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //分割
        String[] s = db.split("_");
        for (int i = 0; i < s.length; i++) {
            if (i != 0) {
                s[i] = s[i].toLowerCase();
            }
            if (i == 0) {
                //拼接
                sb.append(s[0]);
            } else {
                //拼接
                sb.append(s[i].substring(0, 1).toUpperCase()).append(s[i].substring(1));
            }
        }
        return sb.toString();
    }

    /**
     * @param db 实体类对应的值
     * @return 返回转化后的数据库名称
     */
    public static String fieldNameReverseToDBName(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int dbLength = db.length();
        for (int i = 0; i < dbLength; i++) {
            //获取字符
            char c = db.charAt(i);
            //判断
            if (c >= 'A' && c <= 'Z') {
                //遇到大写,就把全部的变小写
                sb.append("_").append((char) (c + 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param db 数据库的表的名字
     * @return
     * @Descripton 这个方法用来将数据库表名转化为规范的类名
     */
    public static String dbNameReverseToClassName(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        //分割
        String[] s = db.split("_");
        for (int i = 0; i < s.length; i++) {
            sb.append(s[i].substring(0, 1).toUpperCase()).append(s[i].substring(1));
        }
        return sb.toString();
    }

    /**
     * @param db
     * @return
     * @Description 将实体类类名转化为数据库的表名
     */
    public static String classNameReverseToDBName(String db) {
        if (db == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < db.length(); i++) {
            char c = db.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                //转化为小写
                c = (char) (c + 32);
                //如果大写字母不是在首个，加_
                if (i != 0) {
                    sb.append('_');
                }
                sb.append(c);
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * @Description 获取反射类和属性
     * @param field
     * @return
     */
    public static String getObj(String field){
        if(field == null){
            return null;
        }
        String[] split = field.split("\\.");
        return split[split.length - 1];
    }

    /**
     * @Description 通过属性名字调用属性方法获取对应的属性值
     * @param name 传入的反射类获取的属性
     * @return 返回获取的属性值
     */
    public static String getMethodByName(Class clazz, Object object, String name) throws Exception {
        //得到get方法
        String fieldName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method getMethod = clazz.getMethod(fieldName);
        //执行get方法,把属性全部转化为String
        String getFieldByMethod = String.valueOf( getMethod.invoke(object));
        return getFieldByMethod;
    }

    /**
     * 把传入的参数转化为set的String类型返回
     * @param name 传入的参数
     * @return 返回该参数的set方法的方法名
     */
    public static String toSet(String name){
        String setName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        return setName.trim();
    }

    public static String getBriefMethodByName(String name){
        String[] split = name.split(".");
        if(split.length == 0){
            return null;
        }
        return split[split.length - 1];
    }

    /**
     * 解决传入数据库/丢失问题
     * @param path 原来的路径
     * @return
     */
    public static String imgsPath(String path){
        StringBuilder bd = new StringBuilder();
        for(int i = 0; i < path.length(); i++){
            char c = path.charAt(i);
            if(c == '\\'){
                //c是\字符
                bd.append('/');
            }else{
                bd.append(c);
            }
        }
        return bd.toString();
    }
}
