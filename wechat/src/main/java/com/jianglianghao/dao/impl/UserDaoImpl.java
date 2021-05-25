package com.jianglianghao.dao.impl;

import com.jianglianghao.dao.UserDao;
import com.jianglianghao.util.JDBCUtils;
import com.jianglianghao.util.StringUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/260:21
 */

public class UserDaoImpl implements UserDao {
    private UserDaoImpl() {
    }

    //内部创建类的对象
    private volatile static  UserDaoImpl UserDaoImpl = null;

    //提供对象,同步方法
    public synchronized static UserDaoImpl getInstance() {
        //同步方法的锁就是当前类本身
        if(UserDaoImpl == null){
            synchronized (UserDaoImpl.class){
                if(UserDaoImpl == null) {
                    UserDaoImpl = new UserDaoImpl();
                }
            }
        }
        return UserDaoImpl;
    }
    @Override
    public <T> List<T> getAllInstance(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //设置数据库的隔离级别,避免脏读
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            //取消自动提交数据
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行，获取结果集
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.newInstance();
                //给T对象属性赋值的过程
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值:通过ResultSet
                    Object columnValue = rs.getObject(i + 1);
                    //通过ResultSetMetaData
                    //获取列的列名：getColumnName() --不推荐使用
                    //获取列的别名：getColumnLabel()
//             String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            conn.commit();
            return list;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    @Override
    public void update(Connection coll, String sql, Object... args) {
        PreparedStatement preparedStatement = null;
        try {
            //设置数据库的隔离级别,避免脏读
            coll.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            //取消自动提交数据
            coll.setAutoCommit(false);
            //2. 预编译sql语句，返回prepareStatement
            preparedStatement = coll.prepareStatement(sql);
            //3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                //第一个从1开始，第二个从0开始
                preparedStatement.setObject(i + 1, args[i]);
            }
            //4. 执行
            preparedStatement.execute();
            //提交事务
            coll.commit();
        } catch (Exception e) {
            try {
                coll.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                coll.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //5. 关闭资源
            JDBCUtils.closeResource(null, preparedStatement);
        }
    }

    @Override
    public <T> T getInstance(Connection coll, Class<T> clazz, String sql, Object... args) {
        coll = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1. 获取连接
            coll = JDBCUtils.getCollection();
            //设置数据库的隔离级别,避免脏读
            coll.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            //取消自动提交数据
            coll.setAutoCommit(false);
            //2. 预编译sql语句，返回prepareStatement
            ps = coll.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValue = rs.getObject(i + 1);

                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columValue);
                }
                return t;
            }
            //提交事务
            coll.commit();
        } catch (Exception e) {
            //回滚
            if(coll != null) {
                try {
                    coll.rollback();
                } catch(SQLException ex) {}
            }
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    @Override
    public <T> List<T> seek(T entity) throws Exception{
        Connection coll = JDBCUtils.myDatasource.getConnection();
        List<String> list = new LinkedList<>();
        //创建拼接对象
        StringBuilder sbd = new StringBuilder();
        sbd.append("select ");
        //获取当前传入的对象的类
        Class<?> aClass = entity.getClass();
        //获取当前类名
        String className = StringUtil.getObj(String.valueOf(aClass));
        //再将类名转化为DB下的表名
        String classNameTODBName = StringUtil.classNameReverseToDBName(className);
        //获取当前传入的类的属性值
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            //获取当前类的特点属性的名字
            String fieldName = declaredFields[i].getName();
            //对fieldName进行转化为数据库的_命名规范
            String dbName = StringUtil.fieldNameReverseToDBName(fieldName);
            //调用get方法获取对象的fieldName的值
            String field = StringUtil.getMethodByName(entity.getClass(), entity, fieldName);
            //对getFieldByMethod进行判断，如果是int属性，未赋值就是0，char[]或者text就是null或者""
            if (!(field.equals("null") || field.equals(" ") || field.equals("0"))) {
                //存入list中作为查询条件
                list.add(dbName + "= '" + field + "'");
            }
            if (i != declaredFields.length - 1) {
                sbd.append(dbName + " " + fieldName + ", ");
            } else {
                sbd.append(dbName + " " + fieldName + " ");
            }
        }
        sbd.append(" from `" + classNameTODBName + "` where ");
        //把list中的数据提取出来进行拼接
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                sbd.append(list.get(i) + " and ");
            } else {
                sbd.append(list.get(i));
            }
        }
        List<T> allInstance = (List<T>)getAllInstance(coll, entity.getClass(), sbd.toString());
        //考虑事务之后要在外面关闭
        try {
            coll.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allInstance;
    }

    @Override
    public <T> void delete(T entity) throws Exception {
        Connection conn = JDBCUtils.myDatasource.getConnection();
        List<String> list = new LinkedList<>();
        //创建拼接对象
        StringBuilder sbd = new StringBuilder();
        sbd.append("delete from `");
        //获取当前传入的对象的类
        Class<?> aClass = entity.getClass();
        //获取当前类名
        String className = StringUtil.getObj(String.valueOf(aClass));
        //再将类名转化为DB下的表名
        String classNameTODBName = StringUtil.classNameReverseToDBName(className);
        sbd.append(classNameTODBName + "`  where ");
        //获取当前传入的类的属性值
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            //获取当前类的特点属性的名字
            String fieldName = declaredFields[i].getName();
            //对fieldName进行转化为数据库的_命名规范
            String dbName = StringUtil.fieldNameReverseToDBName(fieldName);
            //调用get方法获取对象的fieldName的值
            String field = StringUtil.getMethodByName(entity.getClass(), entity, fieldName);
            if (!(field.equals("null") || field.equals(" ") || field.equals("0"))) {
                //存入list中作为查询条件
                list.add("`"+dbName + "` = '" + field + "'");
            }
        }
        //取出list中的条件放入sql中
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                sbd.append(list.get(i) + " and ");
            } else {
                sbd.append(list.get(i) + " ");
            }
        }
        //调用方法
        update(conn, sbd.toString());
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public <T> void add(T entity) throws Exception {
        Connection conn = JDBCUtils.myDatasource.getConnection();
        List<String> list = new LinkedList<>();
        //创建拼接对象
        StringBuilder sbd = new StringBuilder();
        sbd.append("set foreign_key_checks = 0; \n insert into `");//关闭外键约束
        //获取当前传入的对象的类
        Class<?> aClass = entity.getClass();
        //获取当前类名
        String className = StringUtil.getObj(String.valueOf(aClass));
        //再将类名转化为DB下的表名
        String classNameTODBName = StringUtil.classNameReverseToDBName(className);
        sbd.append(classNameTODBName + "` values( ");
        //获取当前传入的类的属性值
        Field[] declaredFields = entity.getClass().getDeclaredFields();

        for (int i = 0; i < declaredFields.length; i++) {
            //获取当前类的特点属性的名字
            String fieldName = declaredFields[i].getName();
            //调用get方法获取对象的fieldName的值
            String field = StringUtil.getMethodByName(entity.getClass(), entity, fieldName);
            if (!(field.equals("null") || field.equals(" ") || field.equals("0"))) {
                //如果不是空，就拼接即可
                list.add("'" + field + "'");
            }
            if (field.equals("null")) {
                list.add("'" + null +"'");
            }
            if (field.equals("0")) {
                list.add("'" + 0 +"'");
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                sbd.append(list.get(i) + ", ");
            } else {
                sbd.append(list.get(i) + " );");
            }
        }
        sbd.append("\n set foreign_key_checks = 1;");
        update(conn, sbd.toString());
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 传入一个实体类，把数据库修改成实体类的属性
     * @param entity 传入类
     * @param <T>
     * @param args 根据什么来改，传入的一定是一个不可变的，比如账号
     * @throws Exception
     */
    @Override
    public <T> void change(T entity, String...args) throws Exception {
        int sum = 0;
        int length = 0;
        Connection conn = JDBCUtils.myDatasource.getConnection();
        List<String> list = new LinkedList<>();
        //创建拼接对象
        StringBuilder sbd = new StringBuilder();
        sbd.append("update `");//关闭外键约束
        //获取当前传入的对象的类
        Class<?> aClass = entity.getClass();
        //获取当前类名
        String className = StringUtil.getObj(String.valueOf(aClass));
        //再将类名转化为DB下的表名
        String classNameTODBName = StringUtil.classNameReverseToDBName(className);
        sbd.append(classNameTODBName + "` set ");
        //获取当前传入的类的属性值
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            //获取当前类的特点属性的名字
            String fieldName = declaredFields[i].getName();
            //调用get方法获取对象的fieldName的值
            String field = StringUtil.getMethodByName(entity.getClass(), entity, fieldName);
            //对不是0或者不是null的值进行赋值
            if (!(field.equals("null") || field.equals(" ") || field.equals("0"))){
                sum ++;
            }
        }
        for (int i = 0; i < declaredFields.length; i++) {
            //获取当前类的特点属性的名字
            String fieldName = declaredFields[i].getName();
            //调用get方法获取对象的fieldName的值
            String field = StringUtil.getMethodByName(entity.getClass(), entity, fieldName);
            //对不是0或者不是null的值进行赋值
            if (!(field.equals("null") || field.equals(" ") || field.equals("0"))) {
                //如果不是空或者是0，就拼接即可
                //获取字段名
                String dbName = StringUtil.fieldNameReverseToDBName(fieldName);
                sbd.append("`"+dbName + "`= '" + field + "' ");
                if (length < sum - 1) {
                    sbd.append(", ");
                }
                length++;
            }
        }
        sbd.append(" where ");
        for(int i = 0; i < args.length; i++){
            String[] split = args[i].split("=");
            String dbName = StringUtil.fieldNameReverseToDBName(split[0]);
            sbd.append( dbName + "= '" + split[1] +"' ");
            if(i + 1 < args.length){
                sbd.append(" and ");
            }else{
                sbd.append("; ");
            }
        }
        update(conn, sbd.toString());
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /*************************************************************************/
    /**
     * @Description 获取连接
     * @verdion 1.0
     */
    public static Connection getCollection() throws Exception {
        //1.读取配置文件中4个基本信息
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
        String driverClass = bundle.getString("driverClass");
        String url = bundle.getString("url");
        String user = bundle.getString("user");
        String password = bundle.getString("password");
        PreparedStatement preparedStatement = null;
        //2. 加载驱动
        Class.forName(driverClass);
        //3. 获取连接
        Connection collection = DriverManager.getConnection(url, user, password);
        return collection;
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

    /**
     * @Description 获取数据库任一表的所有记录
     * @param clazz 反射类
     * @param sql sql语句
     * @param args 填充占位符的参数
     * @param <T> 泛型
     * @return 返回list<T>
     */
    public static <T> List<T> getAllInstances(Class<T> clazz, String sql, Object... args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getCollection();
            ps = conn.prepareStatement(sql);
            for(int i = 0;i < args.length;i++){
                ps.setObject(i + 1, args[i]);
            }

            //执行，获取结果集
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<T>();
            while(rs.next()){
                T t = clazz.newInstance();
                //给T对象属性赋值的过程
                for(int i = 0;i < columnCount;i++){
                    //获取每个列的列值:通过ResultSet
                    Object columnValue = rs.getObject(i + 1);
                    //通过ResultSetMetaData
                    //获取列的列名：getColumnName() --不推荐使用
                    //获取列的别名：getColumnLabel()
//					String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    /**
     * @Description //通用的增删改操作,占位符个数应该和可变形参个数一致
     * @param sql sql语句
     * @param args 参数
     */
    public static void updates(String sql, Object ... args){

        Connection collection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1. 获取连接
            collection  = JDBCUtils.getCollection();
            //2. 预编译sql语句，返回prepareStatement
            preparedStatement = collection.prepareStatement(sql);
            //3. 填充占位符
            for(int i = 0; i < args.length; i++){
                //第一个从1开始，第二个从0开始
                preparedStatement.setObject(i + 1, args[i]);
            }
            //4. 执行
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5. 关闭资源
            JDBCUtils.closeResource(collection,preparedStatement);
        }
    }



    /**
     *
     * @Description 针对于不同的表的通用的查询操作，返回表中的一条记录
     * @author shkstart
     * @date 上午11:42:23
     * @param clazz 反射
     * @param sql sql语句
     * @param args 填充的参数
     * @return 返回一个对象
     */
    public static <T> T getInstance1(Class<T> clazz,String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getCollection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据 :ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columValue = rs.getObject(i + 1);

                    // 获取每个列的列名
                    // String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给t对象指定的columnName属性，赋值为columValue：通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);

        }
        return null;
    }
}
