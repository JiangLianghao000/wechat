<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/21
  Time: 2:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>游客查找用户</title>
    <link type="text/css" rel="stylesheet" href="static/css/travelFindFriend.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2525342_0dtdxoru5sxu.css"/>
</head>
<body>
<div class="content">
    <div class="tit">
        <h1 style="color:white">好友信息</h1>
    </div>
    <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.travelFindFriend.headProtrait}"><%--群头像--%>
    <i class="iconfont iconid" id="iconid" name="weixin"></i> <!-- 将来用来绘制微信前面的图标 -->
    <label id="id" name="id" class="labels">id:${sessionScope.travelFindFriend.id} </label>
    <br/><br/>
    <i class="iconfont icon-xingming" id="icon-xingming" name="icon-xingming"></i> <!-- 将来用来绘制微信前面的图标 -->
    <label id="name" name="name" class="labels">名字：${sessionScope.travelFindFriend.name} </label>
    <br/><br/>
    <i class="iconfont iconzhanghao" id="iconzhanghao" name="iconzhanghao"></i> <!-- 将来用来绘制微信前面的图标 -->
    <label id="account" name="account" class="labels">账号：${sessionScope.travelFindFriend.account}</label>
    <br/><br/>
    <i class="iconfont iconyouxiang" id="iconyouxiang" name="iconyouxiang"></i> <!-- 将来用来绘制微信前面的图标 -->
    <label type="text" name="email" id="email" class="labels">邮箱:${sessionScope.travelFindFriend.email}</label>
    <br/><br/>
    <i class="iconfont iconshenfen" id="iconshenfen" name="iconshenfen"></i> <!-- 将来用来绘制微信前面的图标 -->
    <label name="protiait" id="protrait" class="labels">身份：${sessionScope.travelFindFriend.userKind}</label>
    <br/><br/>
</div>
</body>
</html>
