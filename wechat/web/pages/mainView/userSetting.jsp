<%@ page import="com.jianglianghao.util.PasswordUtil" %>
<%@ page import="javax.xml.transform.Source" %>
<%@ page import="com.jianglianghao.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/3
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/userSetting.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2525342_1jmhssxe30ch.css"/>
    <link rel="shortcut icon" href="#" />
    <title>用户设置界面</title>
    <script>
        $(function(){
            $("#headimg").click(function (){
                $("#btn").removeAttr("disabled");
            })
        });

        function show(f) {
            var file = f.files[0];//获取file组件中的文件
            var reader = new FileReader();//创建文件读取对象
            reader.readAsDataURL(file);//文件读取装换为base64类型
            reader.onload = function (e) {
                //加载完毕之后获取结果赋值给img
                document.getElementById("showimg").src = e.target.result;
            }
        };
    </script>
</head>
<body>
<div class="content">
    <div class="login_box">
        <div class="tit">
            <h1 style="color:white">用户信息</h1>
            <span class="errorMsg" style="color: #ff0000">
                ${ requestScope.msg }
            </span>
        </div>
        <form action="fileServlet?method=updateHead&filename=<%=request.getParameter("account") + "weChat"%>&useraccount=<%=request.getParameter("account")%>"  method="post"  enctype="multipart/form-data">
            <input type="file" id="headimg" name="headimg" class="file1" accept="image/png, image/jpeg, image/gif, image/jpg" onchange="show(this)">
            <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.loginUser.headProtrait}">
            <input type="submit" value="设置" disabled="disabled" class="setHead" id="btn">
        </form>
        <form action="userMSGServlet?method=modifyMSG" method="post">
            <br/><br/><br/>
            <i class="iconfont iconid" id="iconid" name="weixin"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label id="id" name="id" class="labels">${sessionScope.loginUser.id}
            </label>
            <br/><br/>
            <i class="iconfont icon-xingming" id="icon-xingming" name="icon-xingming"></i> <!-- 将来用来绘制微信前面的图标 -->
            <input type="text" name="name" id="name" class="itxt" value=${sessionScope.loginUser.name}>
            <br/><br/>
            <i class="iconfont iconzhanghao" id="iconzhanghao" name="iconzhanghao"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label id="account" name="account" class="labels">${sessionScope.loginUser.account}
            </label>
            <br/><br/>
            <i class="iconfont iconziyuan" id="iconziyuan" name="iconziyuan"></i> <!-- 将来用来绘制微信前面的图标 -->
            <input type="text" name="password" id="password" class="itxt" value=${sessionScope.loginUser.password}>
            <br/><br/>
            <i class="iconfont iconyouxiang" id="iconyouxiang" name="iconyouxiang"></i> <!-- 将来用来绘制微信前面的图标 -->
            <input type="text" name="email" id="email" class="itxt" value="${sessionScope.loginUser.email}">
            <br/><br/>
            <i class="iconfont iconshenfen" id="iconshenfen" name="iconshenfen"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label name="protiait" id="protrait" class="labels">${sessionScope.loginUser.userKind}
            </label>
            <br/><br/>
            <input type="submit" name="submit" id="submit" class="button_css" value="修改信息">
        </form>
    </div>
</div>
</body>
</html>
