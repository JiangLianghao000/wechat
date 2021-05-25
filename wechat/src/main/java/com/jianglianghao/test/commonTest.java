package com.jianglianghao.test;

import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.User;
import com.jianglianghao.util.PasswordUtil;
import com.jianglianghao.util.StringUtil;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/122:13
 */

public class commonTest {
    @Test
    public void test() throws Exception {
        System.out.println(PasswordUtil.decryptAES("Uy6/+jW4eiSZZPs2hvN1bw==", PasswordUtil.key, PasswordUtil.transformation, PasswordUtil.algorithm));
    }

    @Test
    public void test1() throws Exception {
        String pureNum = "-?[0-9]+(\\\\.[0-9]+)?";
        boolean matches = "1242".matches(pureNum);
        System.out.println(matches);
    }

    @Test
    public void test3() {
        String path = "D:/javaCode/web/out/artifacts/wechat_war_exploded/imgs/2021/5/4";
        System.out.println(path.length());
        String substring = path.substring(49);
        System.out.println(substring);
    }

    @Test
    public void test4() throws Exception {
        User user = new User("account=2429890953Aa@");
        user.setPassword("SdiYsuiBtqascJZFVI4qZA==");
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<User> seek = instance.seek(user);
        System.out.println(seek.get(0));
    }

    @Test
    public void test5() throws Exception {
        //正则表达式判断
        String nameRegex = "/^.{3,20}$/";
        String emailRegex = "/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$/";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}";

        String e = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
        String email = "2429890953@qq.com";
        String password = "2429899935#Aa";
        String name = "蒋梁浩a";
        String namePatt1 = "^.{3,15}$" ;
        String emailRegex1 = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
        System.out.println(email.matches(emailRegex1));
        System.out.println(password.matches(passwordRegex));
        System.out.println(name.matches(namePatt1));
    }

    @Test
    public void test7() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        System.out.println("当前时间：" + sdf.format(now));

        //加毫秒数
        Date afterDate = new Date(now .getTime() + 7*24*60*60*1000);
        System.out.println(sdf.format(afterDate));
        String format = sdf.format(afterDate);
        System.out.println(format);
        System.out.println((sdf.parse(format)).getTime());
    }

}
