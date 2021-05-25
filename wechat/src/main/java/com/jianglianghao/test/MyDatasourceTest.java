package com.jianglianghao.test;

import com.jianglianghao.dao.pool.MyDatasource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/260:03
 */
public class MyDatasourceTest {
    @Test
    public void getConnection() throws SQLException {
        Connection coll = new MyDatasource().getConnection();
        System.out.println("连接是 : " + coll);
    }
}