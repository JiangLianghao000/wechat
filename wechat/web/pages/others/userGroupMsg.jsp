<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/12
  Time: 1:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/userGroupMsg.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2537798_7qzxuqxg18.css"/>
    <title>群聊信息</title>
</head>
<body>
<div class="content">
    <div class="tit">
        <h1 style="color:white">群聊信息</h1>
    </div>
    <br>
    <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.findUserByAccountInFriendTipJsp.groupHeadportrait}"><%--群头像--%>
    <i class="iconfont icon-qunzhanghao" id="iconid" name="weixin"></i>
    <input type="text" name="name" id="name" class="itxt" disabled="disabled" value="群账号：${sessionScope.findUserByAccountInFriendTipJsp.groupAccount}"><%--群账号--%>
    <br><br>
    <i class="iconfont icon-qunzhu" id="iconid" name="weixin"></i>
    <input type="text" name="name" id="name" class="itxt" disabled="disabled" value="群主：${sessionScope.groupMainPeople}"><%--群名称--%>
    <br><br>&nbsp;&nbsp;&nbsp;&nbsp;
    <i class="iconfont icon-qunmingpian" id="iconid" name="weixin"></i>&nbsp;
    <input type="text" style="color: darkviolet" name="name" id="name" class="itxt" disabled="disabled" value="群名称：${sessionScope.findUserByAccountInFriendTipJsp.groupName}"><%--群名称--%>
    <br><br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <span>群简介</span>
    <textarea disabled="disabled" class="textArea" id="text" placeholder="${sessionScope.findUserByAccountInFriendTipJsp.groupAnnounce}"></textarea>
</div>
</body>
</html>
