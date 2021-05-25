package com.jianglianghao.dao.pool;

import java.sql.PreparedStatement;
import java.util.ResourceBundle;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 获取配置文件资源
 * @verdion
 * @date 2021/4/2523:51
 */

public class ConnectionConfig {
    //获取配置文件中的参数
    static ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
    static String  driverClass = bundle.getString("driverClass");
    static String url = bundle.getString("url");
    static String user = bundle.getString("user");
    static String password = bundle.getString("password");
    static String poolMaxActiveConnection = bundle.getString("poolMaxActiveConnection");
    static String poolMaxidleConnection = bundle.getString("poolMaxidleConnection");
    static String poolTimeToWait = bundle.getString("poolTimeToWait");
}
