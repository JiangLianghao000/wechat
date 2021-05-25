<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/6
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户信息界面</title>
</head>
<body>
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
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/userSetting.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2525342_rb5azsdts5r.css"/>
    <link rel="shortcut icon" href="#" />
    <title>用户设置界面</title>
</head>
<body>
<div class="content">
    <div class="login_box">
        <div class="tit">
            <h1 style="color:white">用户信息</h1>
        </div>
            <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.findUser.headProtrait}">
        <form action="userMSGServlet?method=modifyMSG" method="post">
            <br/><br/><br/>
            <i class="iconfont iconID" id="iconid" name="weixin"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label id="id" name="id" class="labels">${sessionScope.findUser.id}
            </label>
            <br/><br/>
            <i class="iconfont icon-xingming" id="icon-xingming" name="icon-xingming"></i> <!-- 将来用来绘制微信前面的图标 -->
            <input type="text" name="name" id="name" class="itxt" value=${sessionScope.findUser.name}>
            <br/><br/>
            <i class="iconfont iconzhanghao" id="iconzhanghao" name="iconzhanghao"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label id="account" name="account" class="labels">${sessionScope.findUser.account}
            </label>
            <br/><br/>
            <i class="iconfont iconyouxiang" id="iconyouxiang" name="iconyouxiang"></i> <!-- 将来用来绘制微信前面的图标 -->
            <input type="text" name="email" id="email" class="itxt" value="${sessionScope.findUser.email}">
            <br/><br/>
            <i class="iconfont iconshenfen" id="iconshenfen" name="iconshenfen"></i> <!-- 将来用来绘制微信前面的图标 -->
            <label name="protiait" id="protrait" class="labels">${sessionScope.findUser.userKind}
            </label>
        </form>
    </div>
</div>
</body>
</html>

</body>
</html>
