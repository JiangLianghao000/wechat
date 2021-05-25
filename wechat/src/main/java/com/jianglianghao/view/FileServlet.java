package com.jianglianghao.view;

import com.google.gson.Gson;
import com.jianglianghao.controller.*;
import com.jianglianghao.entity.*;
import com.jianglianghao.util.CommonUtil;
import com.jianglianghao.util.PasswordUtil;
import com.jianglianghao.util.SensitiveWordUtil;
import com.jianglianghao.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import com.jianglianghao.view.BaseServlet;
import sun.management.resources.agent;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static com.jianglianghao.util.storageUtils.*;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/411:23
 */

public class FileServlet extends com.jianglianghao.view.BaseServlet {



    /**
     * 带临时文件的，解决文件名乱码和设置文件最大值问题
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updateHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user =(User) request.getSession().getAttribute("loginUser");
        String account = request.getParameter("useraccount");

        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024 * 1024 * 1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为2M
            upload.setFileSizeMax(1024 * 1024 * 2);

            //设置一次上传的所有文件的总和最大不超过5m，对于多个文件起作用
            upload.setSizeMax(1024 * 1024 * 5);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/imgs");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year + "/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    //把路径写入数据库


                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    user.setHeadProtrait(s);
                    //调用方法把路径存入数据库
                    int i = new UserLoginAndRegistController().modifyUserMSG(user);
                    //存入之后再对密码解码处理方便显示
                    user.setPassword(PasswordUtil.decryptAES(user.getPassword(),
                            PasswordUtil.key, PasswordUtil.transformation, PasswordUtil.algorithm));

                    request.getSession().setAttribute("loginUser", user);
                    //把s加入user_friend中
                    UserFriend userFriend = new UserFriend("friendHeadportrait="+s);
                    new UserMsgController().modifyHead(userFriend, user.getId(), 0);
                    //判断
                    //关闭流
                    os.close();
                    is.close();

                    //删除临时文件
                    item.delete();
                    if (i == 1) {
                        //从新加载界面，提示信息
                        request.setAttribute("msg", "用户头像已更新");
                        request.getRequestDispatcher("/pages/mainView/userSetting.jsp").forward(request, response);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 用于设置用户头像
     *
     * @param request  请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void setGroupHead(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user =(User) request.getSession().getAttribute("loginUser");
        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024 * 1024 * 1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为2M
            upload.setFileSizeMax(1024 * 1024 * 2);

            //设置一次上传的所有文件的总和最大不超过5m，对于多个文件起作用
            upload.setSizeMax(1024 * 1024 * 5);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                } else {
                    Groups groupss = (Groups) request.getSession().getAttribute("findUserByAccountInFriendTipJsp" );
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/groupHeadImgs");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year + "/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    //获取了路径s，存入数据库中
                    Groups groups = new Groups("groupHeadportrait="+s);
                    new UserAndGroupController().modifyGroupHead(groups, request);
                    groupss.setGroupHeadportrait(s);
                    //找到group
                    Groups groups1 = new Groups("userId="+user.getId(), "groupName="+groupss.getGroupName());
                    List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
                    request.getSession().setAttribute("findUserByAccountInFriendTipJsp", allGroups.get(0));
                    response.getWriter().write("已修改");
                    //关闭流
                    os.close();
                    is.close();
                    //删除临时文件
                    item.delete();
                    return;
                }
            }
        } catch (Exception e) {
            response.getWriter().write("出现异常，请联系管理员2429890953@qq.com反馈问题");
            e.printStackTrace();
        }
    }

    /**
     * 朋友圈添加图片
     * @param request
     * @param response
     * @throws Exception
     */
    public void addCirclePicture(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user =(User) request.getSession().getAttribute("loginUser");

        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024*1024*1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为2M
            upload.setFileSizeMax(1024*1024*2);

            //设置一次上传的所有文件的总和最大不超过5m，对于多个文件起作用
            upload.setSizeMax(1024*1024*5);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                    System.out.println(fieldName + " = " + fileValue);
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/ciclePicture");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year +"/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if(!dirFile.exists()){
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    //返回
                    response.getWriter().write(s);
                    //关闭流
                    os.close();
                    is.close();

                    //删除临时文件
                    item.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件下载
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{

    }

    /**
     * 好友聊天界面发送图片
     * @param request
     * @param response
     * @throws Exception
     */
    public void userChatWithFriendAddPicture(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user =(User) request.getSession().getAttribute("loginUser");
        String friendName = request.getParameter("friendName");
        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024*1024*1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为2M
            upload.setFileSizeMax(1024*1024*2);

            //设置一次上传的所有文件的总和最大不超过5m，对于多个文件起作用
            upload.setSizeMax(1024*1024*5);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                    System.out.println(fieldName + " = " + fileValue);
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/userchat/img");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year +"/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if(!dirFile.exists()){
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    //返回
                    response.getWriter().write(s);
                    //调用数据库来添加,记录聊天
                    UserChat userChat = new UserChat(0, user.getName(), friendName, s, "picture");
                    new UserChatController().sendContent(userChat);

                    //关闭流
                    os.close();
                    is.close();

                    //删除临时文件
                    item.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 好友聊天界面发送文件
     * @param request
     * @param response
     * @throws Exception 异常
     */
    public void userChatWithFriendAddFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user =(User) request.getSession().getAttribute("loginUser");
        String friendName = request.getParameter("friendName");

        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024*1024*1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为2M
            upload.setFileSizeMax(1024*1024*10);

            //设置一次上传的所有文件的总和最大不超过5m，对于多个文件起作用
            upload.setSizeMax(1024*1024*10);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                    System.out.println(fieldName + " = " + fileValue);
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/userchat/file");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year +"/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if(!dirFile.exists()){
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    //返回

                    //关闭流
                    os.close();
                    is.close();
                    //删除临时文件
                    item.delete();
                    Gson gson = new Gson();
                    response.getWriter().write(gson.toJson(s));
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * @param request 请求
     * @param response 转发
     * @throws Exception 异常
     */
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //1. 接受参数
        String Url = request.getParameter("filename");
        String filename = request.getParameter("filename").split("/")[request.getParameter("filename").split("/").length - 1];
        //2. 下载，设置两个头和一个流
        //获取文件类型
        String type = getServletContext().getMimeType(filename);
        response.setContentType(type);
        //定义一个代表该文件的路径
        //首先取出files路径
        String realUrl = Url.substring(7);
        String path = this.getServletContext().getRealPath("");
        File file = new File(path + realUrl);
        //判断浏览器类型,解决中文问题
        String agent = request.getHeader("User-Agent");
        if(agent.contains("Firefox")){
            //使用火狐浏览器
            filename = CommonUtil.Base64EcondingFileName(filename);
        }else{
            //iE或其他浏览器
            URLEncoder.encode(filename, "UTF-8");
        }
        //Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename="+filename);
        //设置一个代表了文件输入流
        InputStream is = new FileInputStream(file);
        ServletOutputStream os = response.getOutputStream();

        //两个流对接
        int len = 0;
        byte[] b = new byte[1024];

        while((len = is.read(b))!= -1){
            os.write(b, 0, len);
        }
        is.close();
        os.close();
    }

    /**
     * 群聊发送图片
     * @param request
     * @param response
     * @throws Exception
     */
    public void groupSendPicture(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user =(User) request.getSession().getAttribute("loginUser");
        //获取群聊的名字
        String groupName = request.getParameter("groupName");
        Groups groups = new Groups("groupName="+groupName);
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024 * 1024 * 1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为2M
            upload.setFileSizeMax(1024 * 1024 * 10);

            //设置一次上传的所有文件的总和最大不超过5m，对于多个文件起作用
            upload.setSizeMax(1024 * 1024 * 10);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/userchat/groupImg");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year + "/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    //把路径写入数据库

                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    if(allGroups.size() != 0){
                        //存储进数据库
                        GroupChat groupChat = new GroupChat(0,allGroups.get(0).getGroupAccount(), user.getName(), s, "picture");
                        new GroupChatController().addGroupChat(groupChat);
                    }
                    //判断
                    //关闭流
                    os.close();
                    is.close();

                    //删除临时文件
                    item.delete();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 用户群聊上传文件
     * @param request 请求
     * @param response 转发
     * @throws Exception 异常
     */
    public void userChatWithGroupAddFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user =(User) request.getSession().getAttribute("loginUser");
        //获取群聊的名字
        String groupName = request.getParameter("groupName");
        Groups groups = new Groups("groupName="+groupName);
        List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024 * 1024 * 1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为10M
            upload.setFileSizeMax(1024 * 1024 * 10);

            //设置一次上传的所有文件的总和最大不超过10m，对于多个文件起作用
            upload.setSizeMax(1024 * 1024 * 10);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/userchat/groupFile");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year + "/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    //把路径写入数据库

                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    //判断
                    //关闭流
                    os.close();
                    is.close();

                    //删除临时文件
                    item.delete();
                    Gson gson = new Gson();
                    response.getWriter().write(gson.toJson(s));
                    return;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群聊下载文件
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void downloadGroupFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //1. 接受参数
        String Url = request.getParameter("filename");
        String filename = request.getParameter("filename").split("/")[request.getParameter("filename").split("/").length - 1];
        //2. 下载，设置两个头和一个流
        //获取文件类型
        String type = getServletContext().getMimeType(filename);
        response.setContentType(type);
        //定义一个代表该文件的路径
        //首先取出files路径
        String realUrl = Url.substring(7);
        String path = this.getServletContext().getRealPath("");
        File file = new File(path + realUrl);
        //判断浏览器类型,解决中文问题
        String agent = request.getHeader("User-Agent");
        if(agent.contains("Firefox")){
            //使用火狐浏览器
            filename = CommonUtil.Base64EcondingFileName(filename);
        }else{
            //iE或其他浏览器
            URLEncoder.encode(filename, "UTF-8");
        }
        //Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename="+filename);
        //设置一个代表了文件输入流
        InputStream is = new FileInputStream(file);
        ServletOutputStream os = response.getOutputStream();

        //两个流对接
        int len = 0;
        byte[] b = new byte[1024];

        while((len = is.read(b))!= -1){
            os.write(b, 0, len);
        }
        is.close();
        os.close();
    }

    /**
     * 好友聊天界面发送图片
     * @param request
     * @param response
     * @throws Exception
     */
    public void groupChatAddMeme(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user =(User) request.getSession().getAttribute("loginUser");
        String userName = request.getParameter("userName");
        String groupName = request.getParameter("groupName");
        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();

            //使用临时文件的边界值，大于该值的上传文件会保存在临时文件中。单位：字节,上传完就删了
            //单位：字节，这里设置为1m
            factory.setSizeThreshold(1024*1024*1);

            //设置临时文件
            String tempPath = this.getServletContext().getRealPath("/temp");
            File temp = new File(tempPath);
            factory.setRepository(temp);

            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);

            //设置每一个item的头部字符的编码，可以解决了文件名的中文乱码问题
            upload.setHeaderEncoding("UTF-8");

            //设置单个文件上传的最大值为2M
            upload.setFileSizeMax(1024*1024*2);

            //设置一次上传的所有文件的总和最大不超过5m，对于多个文件起作用
            upload.setSizeMax(1024*1024*5);

            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString("UTF-8");
                    System.out.println(fieldName + " = " + fileValue);
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //为解决文件名重复问题，添加系统时间
                    fileName = System.currentTimeMillis() + fileName;
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/userchat/groupMeme/userAdd");

                    //获取当前系统事件
                    Calendar now = Calendar.getInstance();
                    //获取年月日
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    path = path + "/" + year +"/" + month + "/" + day;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if(!dirFile.exists()){
                        dirFile.mkdirs();
                    }

                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    String name = path + "\\" + fileName;
                    //从虚拟路径开始截取
                    String s = "/files" + StringUtil.imgsPath(name).substring(49);
                    //存入表情包数据库
                    //找到group
                    Groups groups = new Groups("groupName="+groupName);
                    List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
                    if(allGroups.size() != 0){
                        //找到group的账号
                        Groups atGroup = allGroups.get(0);
                        GroupFile groupFile = new GroupFile(0, atGroup.getGroupAccount(), "picture",
                                s, user.getAccount(), user.getName());
                        new GroupChatController().addMeme(groupFile);
                        response.getWriter().write(new Gson().toJson(s));
                    }
                    //关闭流
                    os.close();
                    is.close();

                    //删除临时文件
                    item.delete();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
