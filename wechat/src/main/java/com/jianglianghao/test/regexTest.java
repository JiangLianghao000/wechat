package com.jianglianghao.test;

import org.junit.Test;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/10:58
 */

public class regexTest {
    @Test
    public void test(){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}";
        System.out.println("2498Aa*aaaaaaa".matches(regex));
    }
}
