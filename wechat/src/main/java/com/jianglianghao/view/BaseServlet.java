package com.jianglianghao.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description 通用的servlet类
 * @verdion
 * @date 2021/4/280:58
 */

public class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //解决请求乱码问题
        req.setCharacterEncoding("UTF-8");
        //解决响应乱码问题
        resp.setContentType("text/html;charset=UTF-8");
        //判断用户行为
        String MethodName = req.getParameter("method");
        try {
            Method methodName = this.getClass().getMethod(MethodName, HttpServletRequest.class, HttpServletResponse.class);
            methodName.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
