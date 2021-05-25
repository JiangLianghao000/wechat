package com.jianglianghao.dao.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 数据源的连接池，实现池
 * @verdion
 * @date 2021/4/2023:25
 */

public class MyDatasource extends com.jianglianghao.dao.pool.AbstractMyDataSource {

    //装连接
    //空闲连接
    private static final List<com.jianglianghao.dao.pool.ConnectionProxy> idleConnection = new LinkedList<>();

    //激活连接
    private static final List<com.jianglianghao.dao.pool.ConnectionProxy> activeConnection = new LinkedList<>();

    //获取对象的时候用来同步用，相当于监视器
    private static final Object monitor = new Object();


    /**
     * 覆盖父类方法，返回一个动态代理连接
     *
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(super.getDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        com.jianglianghao.dao.pool.ConnectionProxy connectionProxy = getConnectionProxy(super.getUser(), super.getPassword());
        return connectionProxy.getProxyConnection();
    }

    /**
     * 获取连接
     *
     * @param username
     * @param password
     * @return
     */
    public com.jianglianghao.dao.pool.ConnectionProxy getConnectionProxy(String username, String password) throws SQLException {
        //等待
        boolean wait = false;

        com.jianglianghao.dao.pool.ConnectionProxy connectionProxy = null;
        //刚开始是没有连接的
        while (connectionProxy == null) {
            //同步线程安全
            synchronized (monitor) {
                //如果空闲连接不为空,就是有空闲连接可以用，可以直接获取连接
                if (!idleConnection.isEmpty()) {
                    connectionProxy = idleConnection.remove(0);
                } else {
                    //如果空闲连接没有连接可以使用，需要创建新的连接来获取，这时候就要考虑最大连接池够不够了
                    if (activeConnection.size() < super.getPoolMaxActiveConnection()) {
                        //如果当前激活的连接数小于配置的最大连接数，可以创建，否则不能创建
                        connectionProxy = new com.jianglianghao.dao.pool.ConnectionProxy(super.getConnection(), this);
                    }
                    //否则是不能创建新连接的，需要等待，等 poolTimeToWait = 30000 毫秒

                }
            }

            if (!wait) {
                wait = true;
            }

            if (connectionProxy == null) {
                try {
                    //连接对象是空的，就需要等待
                    monitor.wait(super.getPoolTimeToWait());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //万一等待被线程打断，退出循环
                    break;
                }
            }
        }
        //while循环过后判断有没有获取到
        if (connectionProxy != null) {
            //连接对象不为空，说明拿到连接了,放入active中
            activeConnection.add(connectionProxy);
        }
        return connectionProxy;
    }

    /**
     * 关闭连接，不是把连接关闭，而是把连接放入归还的连接池
     * @param connectionProxy
     */
    public void closeConnection(com.jianglianghao.dao.pool.ConnectionProxy connectionProxy) {
        synchronized (monitor){
            //关闭连接，把激活状态的连接变成空闲连接
            activeConnection.remove(connectionProxy);
            //放入空闲连接池中
            if(idleConnection.size() < super.getPoolMaxidleConnection()){
                idleConnection.add(connectionProxy);
            }
            //唤醒等待获取空闲连接的线程
            monitor.notify();
        }
    }
}
