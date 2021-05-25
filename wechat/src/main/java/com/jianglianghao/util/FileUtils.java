package com.jianglianghao.util;

import com.jianglianghao.view.BaseServlet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/322:57
 */

@MultipartConfig
public class FileUtils extends BaseServlet {

    /**
     * 带临时文件的
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updatefile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString();
                    System.out.println(fieldName + " = " + fileValue);
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/imgs");
                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
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
     * 不带临时文件的
     * @param request
     * @param response
     * @throws Exception
     */
    public void updateFile1(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //1. 查看是否是Multipart请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("当前请求不支持文件上传");
        }
        try {
            //2.创建一个FileItem工厂，把文件存到工厂或磁盘中
            DiskFileItemFactory factory = new DiskFileItemFactory();


            //3. 创建文件上传核心组件
            ServletFileUpload upload = new ServletFileUpload(factory);
            //4. 解析请求,获取到所有的item
            List<FileItem> items = upload.parseRequest(request);
            //5. 遍历
            for (FileItem item : items) {
                //判断是否是普通表单项，就是没有文件上传的
                if (item.isFormField()) {
                    //获取表单项名字
                    String fieldName = item.getFieldName();
                    //获取表单项的值
                    String fileValue = item.getString();
                    System.out.println(fieldName + " = " + fileValue);
                } else {
                    //是文件上传,获取文件名
                    String fileName = item.getName();
                    //文件输入流，其中有上传文件的内容
                    InputStream is = item.getInputStream();
                    //创建path,获取文件保存在服务器的路径
                    String path = this.getServletContext().getRealPath("/imgs");
                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                    //关闭流
                    os.close();
                    is.close();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 带临时文件的，解决文件名乱码和冲突问题
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updatefile2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    String path = this.getServletContext().getRealPath("/imgs");
                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
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
     * 带临时文件的，解决文件名乱码和设置文件最大值问题
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updatefile5(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    String path = this.getServletContext().getRealPath("/imgs");
                    //新建文件，用原来的文件名和自己定一个保存路径
                    File descFile = new File(path, fileName);
                    OutputStream os = new FileOutputStream(descFile);
                    //将输入流中的数据写出到输出流中
                    int len = -1;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
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
     * 带临时文件的，解决文件名乱码和分目录管理
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updatefile6(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    String path = this.getServletContext().getRealPath("/imgs");

                    //获取当前系统事件
                    Date date = new Date();
                    //格式化日期
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String now = sdf.format(date);
                    path = path + "/" + now;

                    //该目录不存在就创建
                    File dirFile = new File(path);
                    if(!dirFile.exists()){
                        dirFile.mkdir();
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


    /*** 带临时文件的，解决文件名乱码和分目录管理（按年月日管理）
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void updatefile7(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    String path = this.getServletContext().getRealPath("/imgs");

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
     * 文件输出
     * @param request
     * @param response
     * @throws Exception
     */
    public void downloan(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        //修改响应的头部属性content-disposition为attachment
        String filename = "蒋梁浩";
        //打散：按当前的字符编码打散
        byte[] bytes = filename.getBytes("UTF-8");
        //组装：按目标浏览器字符编码组装
        filename = new String(bytes, "ISO8859-1");
        response.setHeader("content-disposition", "attachment；filename=" + filename);
        //TODO 获取服务端资源的输入流，资源地址不定
        InputStream is = this.getServletContext().getResourceAsStream("resourse");

        //获取输出流
        ServletOutputStream os = response.getOutputStream();

        int len = -1;
        byte[] buf = new byte[1024];
        while ((len = is.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
    }

}
