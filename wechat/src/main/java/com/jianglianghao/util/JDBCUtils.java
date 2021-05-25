package com.jianglianghao.util;
import com.jianglianghao.dao.pool.MyDatasource;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 操作数据库的工具类
 * @verdion 1.0
 * @date 2021/3/31 1:03
 */

public class JDBCUtils {
    public static MyDatasource myDatasource = new MyDatasource();
    /**
     * @Description 获取连接
     * @verdion 1.0
     */
    public static Connection getCollection() throws Exception {
        Connection connection = myDatasource.getConnection();
        return connection;
    }

    /**
     * @Description 关闭资源
     * @verdion 1.0
     */
    public static void closeResource(Connection collection, Statement preparedStatement){
        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(collection != null){
            try {
                collection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * @Description 关闭资源
     * @verdion 2.0
     */
    public static void closeResource(Connection collection, Statement preparedStatement, ResultSet resultSet){
        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(collection != null){
            try {
                collection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
