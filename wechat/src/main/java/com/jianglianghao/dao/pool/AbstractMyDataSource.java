package com.jianglianghao.dao.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 抽象类，只有连接
 * @verdion
 * @date 2021/4/2022:53
 */

public class AbstractMyDataSource implements com.jianglianghao.dao.pool.MyDataSourceInterface {

    private  String url = com.jianglianghao.dao.pool.ConnectionConfig.url;

    private String driver = com.jianglianghao.dao.pool.ConnectionConfig.driverClass;

    private String user = com.jianglianghao.dao.pool.ConnectionConfig.user;

    private String password = com.jianglianghao.dao.pool.ConnectionConfig.password;


    //最大激活数,就是最大的正在使用的连接数
    private int poolMaxActiveConnection = Integer.parseInt(com.jianglianghao.dao.pool.ConnectionConfig.poolMaxActiveConnection);

    //最大的空闲连接数
    private int poolMaxidleConnection = Integer.parseInt(com.jianglianghao.dao.pool.ConnectionConfig.poolMaxidleConnection);

    //从连接池中获取一个连接获取连接要等多久时间
    private int poolTimeToWait = Integer.parseInt(com.jianglianghao.dao.pool.ConnectionConfig.poolTimeToWait);

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPoolMaxActiveConnection() {
        return poolMaxActiveConnection;
    }

    public int getPoolMaxidleConnection() {
        return poolMaxidleConnection;
    }

    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(user, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return doGetConnection(username, password);
    }

    /**
     * 获取数据库连接
     * @param username
     * @param password
     * @return
     */
    private Connection doGetConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }




}
