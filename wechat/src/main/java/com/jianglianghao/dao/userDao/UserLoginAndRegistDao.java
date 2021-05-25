package com.jianglianghao.dao.userDao;

import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.dao.impl.UserDaoImpl;
import com.jianglianghao.entity.Ban;
import com.jianglianghao.entity.User;
import com.jianglianghao.util.PasswordUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 处理用户登陆和注册的逻辑
 * @verdion
 * @date 2021/4/3021:09
 */

public class UserLoginAndRegistDao {

    /**
     * 用于检测用户信息是否存在
     *
     * @param user 用户类
     * @return 返回查找结果信息
     */
    public CheckMSG userCheckMSGDao(User user) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        CheckMSG checkMSG = new CheckMSG();
        //获取数据库中的数据
        List<User> seek = instance.seek(user);
        //判断是否有值
        if (seek.size() != 0) {
            checkMSG.setCheckResult("exist");
            //证明该数据已存在数据库
            return checkMSG;
        }
        checkMSG.setCheckResult("no_exist");
        return checkMSG;
    }

    /**
     * 用户登陆检测
     *
     * @param user
     * @param req
     * @return
     * @throws Exception
     */
    public CheckMSG userLoginDao(User user, HttpServletRequest req) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        CheckMSG checkMSG = new CheckMSG();
        //获取数据库中的数据
        List<User> seek = instance.seek(user);
        //用户账号密码正确而且激活了
        //首先判断是否被封号了
        if(seek.size() != 0 ){
            String name = seek.get(0).getName();
            List<Ban> bans = new ManagerDao().finaBan(new Ban("beBannedPeople=" + name));
            if(bans.size() != 0){
                checkMSG.setCheckResult("beBanned");
                return checkMSG;
            }
        }
        //判断是不是游客，游客不能登陆
        if(seek.size() != 0 && seek.get(0).getUserKind().equals("travel")){
            checkMSG.setCheckResult("travel");
            return checkMSG;
        }
        if (seek.size() != 0 && seek.get(0).getIsActive().equals("1")) {
            //先清空
            req.getSession().setAttribute("loginUser", seek.get(0));
            checkMSG.setCheckResult("exist");
            //seek中只存在一个账号，存入map中,并且对密码解析
            String decryPassword = PasswordUtil.decryptAES(seek.get(0).getPassword(), PasswordUtil.key, PasswordUtil.transformation, PasswordUtil.algorithm);
            seek.get(0).setPassword(decryPassword);
            //证明该数据已存在数据库
            return checkMSG;
        }
        checkMSG.setCheckResult("no_exist");
        return checkMSG;
    }

    /**
     * 用户注册
     *
     * @param user 用户对象
     * @return 返回注册提示
     * @throws Exception
     */
    public CheckMSG userRegistDao(User user) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        CheckMSG checkMSG = new CheckMSG();
        instance.add(user);
        List<User> seek = instance.seek(user);
        if (seek.size() != 0) {
            //证明此时存在数据库中了
            checkMSG.setCheckResult("别忘记激活账号");
        } else {
            checkMSG.setCheckResult("注册失败");
        }
        return checkMSG;
    }

    /**
     * 通过账号修改用户信息
     * @param user
     * @return
     * @throws Exception
     */
    public int modifyUserMSG(User user) throws Exception{
        //调用change方法
        UserDaoImpl instance = UserDaoImpl.getInstance();
        //加密存储
        if(user.getPassword() != null){
            user.setPassword(PasswordUtil.encryptAES(user.getPassword()));
        }
        instance.change(user, "account=" + user.getAccount());
        return 1;
    }

    /**
     * 通过简短的信息获取user的全部信息
     * @param user
     * @return
     * @throws Exception
     */
    public List<User> getUser(User user) throws Exception {
        UserDaoImpl instance = UserDaoImpl.getInstance();
        //调用方法
        List<User> seek = instance.seek(user);
        return seek;
    }

    /**
     * 调用方法首先对user名检测，如果name不重复就调用方法修改
     * @param user
     * @return
     */
    public CheckMSG modifyMSG(User user, HttpServletRequest request) throws Exception {
        User user2 = (User)request.getSession().getAttribute("loginUser");
        //对密码加密
        user.setPassword(PasswordUtil.encryptAES(user.getPassword()));

        CheckMSG checkMSG = new CheckMSG();
        UserDaoImpl instance = UserDaoImpl.getInstance();
        //调用方法,创建一个只有name的user
        User user1 = new User("name=" + user.getName());
        List<User> seek = instance.seek(user1);
        //每个人的名字只有一个唯一的，查到的也只有一个
        if(seek.size() == 0){
            //对密码解码
            String decryptAES = PasswordUtil.decryptAES(user.getPassword(), PasswordUtil.key, PasswordUtil.transformation, PasswordUtil.algorithm);
            //没找到，证明可修改
            //把修改后的信息传入loginStorage中
            user2.setEmail(user.getEmail());
            user2.setName(user.getName());
            user2.setPassword(decryptAES);
            //判断两个账户是否相同，相同了就证明用户没有修改名字，直接modify即可
            instance.change(user, "account=" +user2.getAccount());
            checkMSG.setCheckResult("已修改");
            return checkMSG;
        }
        //找到了但是和正在修改的人的名字一样
        if(seek.size()!=0 && seek.get(0).getAccount().equals(user2.getAccount())){
            //对密码解码
            String decryptAES = PasswordUtil.decryptAES(user.getPassword(), PasswordUtil.key, PasswordUtil.transformation, PasswordUtil.algorithm);
            //没找到，证明可修改
            //把修改后的信息传入loginStorage中
            user2.setEmail(user.getEmail());
            user2.setName(user.getName());
            user2.setPassword(decryptAES);
            //判断两个账户是否相同，相同了就证明用户没有修改名字，直接modify即可
            instance.change(user, "account=" + user2.getAccount());
            checkMSG.setCheckResult("已修改");

            return checkMSG;
        }else{
            //证明两个账户不同，要修改的名字重复
            checkMSG.setCheckResult("名字已存在");
            return checkMSG;
        }
    }

    /**
     * 找到所有的用户
     * @return list集合
     * @throws Exception 异常
     */
    public List<User> getAllUser() throws Exception{
        String sql = "select id id, name name, account account, email email, user_kind userKind, head_protrait headProtrait from user";
        UserDaoImpl instance = UserDaoImpl.getInstance();
        List<User> allInstances = instance.getAllInstances(User.class, sql);
        return allInstances;
    }
}
