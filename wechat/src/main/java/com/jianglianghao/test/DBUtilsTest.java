package com.jianglianghao.test;

import com.jianglianghao.controller.CircleController;
import com.jianglianghao.dao.impl.UserDaoImpl;
import  com.jianglianghao.entity.User;
import  com.jianglianghao.entity.Circle;
import com.jianglianghao.util.JDBCUtils;
import com.jianglianghao.util.MailUtil;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jianglianghao.util.storageUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/260:08
 */

public class DBUtilsTest {
    @Test
    public void test1() throws Exception {
        List<com.jianglianghao.entity.User> seek = UserDaoImpl.getInstance().seek(new com.jianglianghao.entity.User("account=2429890953Aa@", "password=SdiYsuiBtqascJZFVI4qZA"));
        System.out.println(seek);
    }

    @Test
    public void test2() throws Exception{
        UserDaoImpl.getInstance().add(new com.jianglianghao.entity.User("name=jlh", "account=24274124Aa@", "password=sdsadadsa12141", "email=2429890953Aa@", "userKind=user", "emailPermission=public", "is_active=0"));
    }

    @Test
    public void email() throws Exception {
        User user = new User("name=jlh", "account=24274124Aa@", "password=sdsadadsa12141", "email=2429890953@qq.com", "userKind=user", "emailPermission=public", "is_active=0");
        MailUtil.sendMail(user);
    }

    @Test
    public void test4() throws Exception {
        //子查询
        String sql = " \n" +
                " select content_id contentId, user_name userName, user_account userAccount, content, content_permission" +
                " contentPermission, time, location  from circle \n" +
                " where user_name = (\n" +
                "\t\t select name \n" +
                "\t\t\tfrom `user`\n" +
                "\t\t\twhere id = (\n" +
                "\t\t\t\t\tselect friend_id friendId from user_friend\n" +
                "\t\t\t\t\t\twhere user_id= ? and state = 'friend'\n" +
                "\t\t)\n" +
                " )";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<Circle> user = instance.getAllInstance(null, Circle.class, sql, 15);
        System.out.println(user);
    }

    @Test
    public void test5() throws Exception{
        String sql = " select user_id userId\n" +
                " from user_in_group \n" +
                " where state='wait_for_add' \n" +
                " and group_identity = '群员'\n" +
                " and group_account in (\n" +
                "\t\tselect `group_account` \n" +
                "\t\tfrom `user_in_group` \n" +
                "\t\twhere (group_account = ? AND group_identity = '群主')\n" +
                "\t\tor \t\t\n" +
                "\t\t(group_account = ? AND group_identity = '管理员')\n" +
                "\n" +
                " );";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<com.jianglianghao.entity.UserInGroup> allInstance = instance.getAllInstance(null, com.jianglianghao.entity.UserInGroup.class, sql, "2429890953Aa@", "2429890953Aa");
        System.out.println(allInstance);
    }

    @Test
    public void test6() throws Exception {
        Set<String> sensitiveWordSet = new HashSet<>();
        com.jianglianghao.entity.DemandSensitiveWord demandSensitiveWord = new com.jianglianghao.entity.DemandSensitiveWord();
        String sql = "select * from demand_sensitive_word";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<com.jianglianghao.entity.DemandSensitiveWord> allInstance = instance.getAllInstance(null, com.jianglianghao.entity.DemandSensitiveWord.class, sql);
        //遍历list,存入set
        for(com.jianglianghao.entity.DemandSensitiveWord ds : allInstance){
            sensitiveWordSet.add(ds.getBadword());
        }
    }

    @Test
    public void test7() throws Exception {
            //证明是最后一页，调用方法找到最后一层
            String sql = "select id id, content_id contentId, like_people likePeople, beliked_people belikedPeople  from `circle_like` where content_id= '21' and like_people= '蒋梁浩小号' and beliked_people= '蒋梁浩小号'";
            UserDaoImpl instance = UserDaoImpl.getInstance();
            List<com.jianglianghao.entity.CircleLike> allInstance = instance.getAllInstance(null, com.jianglianghao.entity.CircleLike.class, sql);
            System.out.println(allInstance);

    }
}
