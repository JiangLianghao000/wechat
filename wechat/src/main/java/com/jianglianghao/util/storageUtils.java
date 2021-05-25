package com.jianglianghao.util;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.User;
import org.junit.Test;

import java.sql.Connection;
import java.util.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 用于存储数据的工具类，发送信息的
 * @verdion
 * @date 2021/5/318:14
 */

public class storageUtils {


    //用于存储敏感词汇的方法
    public static Set<String> getSensitiveWordsSet(){
        Set<String> sensitiveWordSet = new HashSet<>();
        com.jianglianghao.entity.DemandSensitiveWord demandSensitiveWord = new com.jianglianghao.entity.DemandSensitiveWord();
        String sql = "select * from demand_sensitive_word";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<com.jianglianghao.entity.DemandSensitiveWord> allInstance = instance.getAllInstances(com.jianglianghao.entity.DemandSensitiveWord.class, sql);
        //遍历list,存入set
        for(com.jianglianghao.entity.DemandSensitiveWord ds : allInstance){
            sensitiveWordSet.add(ds.getBadword());
        }
        return sensitiveWordSet;
    }


    //用于存储在circle界面找到了所有的评论中的评论
    public static List<List<com.jianglianghao.entity.CircleCommentInComment>> findOneCircleComment = new LinkedList<List<com.jianglianghao.entity.CircleCommentInComment>>();

    //用于存储相关评论的条数
    public static int ConnectRecallNumber=0;

    //用于保存管理员登陆界面的验证码
    public static String managerLoginCode;

    //用于存储超管界面的验证码
    public static String superManagerLoginCode;


    //用于存储游客登陆的验证码
    public static String travelLoginCode;
}
