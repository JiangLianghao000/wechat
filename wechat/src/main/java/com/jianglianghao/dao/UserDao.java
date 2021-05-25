package com.jianglianghao.dao;

import java.sql.Connection;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 存放一些通用的整改改查的操作的接口
 * @verdion
 * @date 2021/4/260:11
 */

public interface UserDao {
    /**
     * @Description 获取数据库任一表的所有记录
     * @param clazz 反射类
     * @param sql sql语句
     * @param args 填充占位符的参数
     * @param <T> 泛型
     * @param conn 连接
     * @return 返回list<T>
     */
     public  <T> List<T> getAllInstance (Connection conn, Class<T> clazz, String sql, Object... args) throws Exception;

    /**
     * @Description //通用的增删改操作,占位符个数应该和可变形参个数一致
     * @param sql sql语句
     * @param conn 连接
     * @param args 参数
     */
    public  void update(Connection conn, String sql, Object ... args);

    /**
     *
     * @Description 针对于不同的表的通用的查询操作，返回表中的一条记录
     * @author shkstart
     * @date 上午11:42:23
     * @param clazz 反射
     * @param conn 连接
     * @param sql sql语句
     * @param args 填充的参数
     * @return 返回一个对象
     */
    public  <T> T getInstance(Connection conn, Class<T> clazz,String sql, Object... args);

    /**
     * 传入一个类对象，进行通用的查找操作
     * @param entity 实体类
     * @param <T>
     * @return 查询结果List
     */
    public  <T> List<T> seek(T entity) throws Exception;

    /**
     * @param entity 传入的实体类对象
     * @throws Exception 抛异常
     * @Description 传入一个对象，根据对象的属性进行删除
     */
    public  <T> void delete(T entity) throws Exception;

    /**
     * @param entity 对象类
     * @param <T>    泛型对象
     * @throws Exception 抛出异常
     * @Description 通用的增加操作
     */
    public  <T> void add(T entity) throws Exception;

    /**
     * @param entity 传入类
     * @param <T>    泛型方法
     * @throws Exception 异常
     * @Description 通用的修改操作，根据传入的entity对象，赋值了的就操作，否则不操作。
     */
    public  <T> void change(T entity,String...args) throws Exception;

    public static <T> List<T> getAllInstances(Class<T> clazz, String sql, Object... args) throws Exception{
        return null;
    };

}
