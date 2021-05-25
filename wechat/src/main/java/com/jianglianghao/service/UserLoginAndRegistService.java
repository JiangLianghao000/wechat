package com.jianglianghao.service;

import com.jianglianghao.bean.CheckMSG;
import com.jianglianghao.dao.userDao.UserLoginAndRegistDao;
import com.jianglianghao.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.jianglianghao.util.CommonUtil.judgeMSG;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/3021:00
 */

public class UserLoginAndRegistService {
    //调用数据库方法对用户信息检测
    UserLoginAndRegistDao userLoginAndRegistDao = new UserLoginAndRegistDao();

    /**
     * 对用户信息检测
     * @param user
     * @return
     * @throws Exception
     */
    public CheckMSG checkMSG(User user) throws Exception {
        CheckMSG checkMSG = userLoginAndRegistDao.userCheckMSGDao(user);
        return checkMSG;
    }

    /**
     * 对用户登陆进行检测
     * @param user
     * @param req
     * @return
     */
    public CheckMSG userLogin(User user, HttpServletRequest req) throws Exception {
        boolean judgeMSG = judgeMSG(user.getAccount());
        boolean judgeMSG1 = judgeMSG(user.getPassword());
        CheckMSG checkMSG1  = new CheckMSG();
        if(judgeMSG == true || judgeMSG1 == true){
            checkMSG1.setCheckResult("is_null");
            return checkMSG1;
        }
        return new UserLoginAndRegistDao().userLoginDao(user, req);
    }

    /**
     * 对用户注册管理
     * @param user
     * @return 返回checkMSG
     * @throws Exception
     */
    public CheckMSG userRegist(User user) throws Exception{
        CheckMSG checkMSG = userLoginAndRegistDao.userRegistDao(user);
        return checkMSG;
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     * @throws Exception
     */
    public int modifyUserMSG(User user) throws Exception{
        return new UserLoginAndRegistDao().modifyUserMSG(user);
    }

    public List<User> getUser(User user) throws Exception {
        return new UserLoginAndRegistDao().getUser(user);
    }

    public CheckMSG modifyMSG(User user, HttpServletRequest request) throws Exception {
        CheckMSG checkMSG = new CheckMSG();
        //正则表达式判断
        String nameRegex = "^.{3,20}$";
        String emailRegex = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,16}";
        //邮箱验证
        if(user.getEmail().matches(emailRegex) == false){
            checkMSG.setCheckResult("邮箱格式不正确");
            return checkMSG;
        }
        //密码格式
        if(user.getPassword().matches(passwordRegex) == false){
            checkMSG.setCheckResult("密码格式不正确");
            return checkMSG;
        }
        //用户名格式
        if(user.getName().matches(nameRegex) == false){
            checkMSG.setCheckResult("名字格式不正确");
            return checkMSG;
        }
        //如果格式都正确，检测用户名是否已存在
        return new UserLoginAndRegistDao().modifyMSG(user, request);
    }
}
