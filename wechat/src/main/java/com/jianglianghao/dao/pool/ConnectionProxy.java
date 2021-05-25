package com.jianglianghao.dao.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 采用动态代理实现对数据库的连接
 * @verdion
 * @date 2021/4/2023:01
 */

public class ConnectionProxy implements InvocationHandler {

    //真正连接
    private static  Connection realConnection;

    //代理连接
    private static  Connection proxyConnection;

    //持有数据源对象
    private static com.jianglianghao.dao.pool.MyDatasource myDataSource;

    /**
     * 构造方法
     *
     * @param realConnection 真实连接
     * @param myDataSource   数据源
     */
    public ConnectionProxy(Connection realConnection, com.jianglianghao.dao.pool.MyDatasource myDataSource) {

        //初始化真实连接和数据源
        this.realConnection = realConnection;
        this.myDataSource = myDataSource;

        //初始化代理连接
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                this);
    }

    public static Connection getRealConnection() {
        return realConnection;
    }

    public static void setRealConnection(Connection realConnection) {
        ConnectionProxy.realConnection = realConnection;
    }

    public static Connection getProxyConnection() {
        return proxyConnection;
    }

    public static void setProxyConnection(Connection proxyConnection) {
        ConnectionProxy.proxyConnection = proxyConnection;
    }

    public static com.jianglianghao.dao.pool.MyDatasource getMyDataSource() {
        return myDataSource;
    }

    public static void setMyDataSource(com.jianglianghao.dao.pool.MyDatasource myDataSource) {
        ConnectionProxy.myDataSource = myDataSource;
    }

    /**
     * 当调用Connection对象里面的方法的时候，首先会被该invoke方法拦截
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取到当前调用了Connection对象的什么方法
        String methodName = method.getName();
        if (methodName.equals("close")) {
            //TODO :把连接归还到连接池
            myDataSource.closeConnection(this);
            return null;
        }else{
            //其他方法就直接调用，不拦截，直接执行
            return method.invoke(realConnection, args);
        }
    }
}
