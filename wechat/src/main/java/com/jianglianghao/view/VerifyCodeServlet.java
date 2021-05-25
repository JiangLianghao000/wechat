package com.jianglianghao.view;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/4/2721:33
 */

public class VerifyCodeServlet extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public VerifyCodeServlet() {
        super();
    }

    public void destroy() {
        super.destroy();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        //设置浏览器不缓存本页
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        //生成验证码，写入用户session
        String verifyCode= com.jianglianghao.view.VerifyCode.generateTextCode(com.jianglianghao.view.VerifyCode.TYPE_NUM_UPPER,4,"0oOilJI1");
        request.getSession().setAttribute(com.jianglianghao.view.VerifyCode.VERIFY_TYPE_COMMENT,verifyCode);

        //输出验证码给客户端
        response.setContentType("image/jpeg");
				/*
				    textCode 文本验证码
					width 图片宽度
					height 图片高度
					interLine 图片中干扰线的条数
					randomLocation 每个字符的高低位置是否随机
					backColor 图片颜色，若为null，则采用随机颜色
					foreColor 字体颜色，若为null，则采用随机颜色
					lineColor 干扰线颜色，若为null，则采用随机颜色
				*/
        BufferedImage bim= com.jianglianghao.view.VerifyCode.generateImageCode(verifyCode, 70, 22, 15,true,Color.WHITE,Color.BLACK,null);
        ServletOutputStream out=response.getOutputStream();
        ImageIO.write(bim, "JPEG",out);
        try{
            out.flush();
        }finally{
            out.close();
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }

    public void init() throws ServletException {
        // Put your code here
    }

}

