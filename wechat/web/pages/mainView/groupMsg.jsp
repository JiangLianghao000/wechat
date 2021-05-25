<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/11
  Time: 0:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/groupMsg.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2537798_7qzxuqxg18.css"/>
    <title>查找的群聊信息</title>
    <script>
        $(function (){
            $("#button_1").click(function (){
                var r = confirm("你确定要请求添加吗？");
                //点击添加群聊
                if(r == true){
                    $.post("userAndGroupServlet?method=addGroup", function (date){
                        if(date == "1"){
                            $("#tip").html("已添加群聊，等待同意");
                        }else{
                            alert("添加失败，请联系管理员修复bug");
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
    <div class="content">
        <div class="tit">
            <h1 style="color:white">群聊信息</h1>
        </div>
        <br>
        <img id="showimg" name="showimg" class="head_portrait" src="${requestScope.findGroup.groupHeadportrait}"><%--群头像--%>
        <i class="iconfont icon-qunzhanghao" id="iconid" name="weixin"></i>
        <input type="text" name="name" id="name" class="itxt" disabled="disabled" value="群名：${requestScope.findGroup.groupAccount}"><%--群账号--%>
        <br><br>
        <i class="iconfont icon-qunmingpian" id="iconid" name="weixin"></i>
        <input type="text" name="name" id="name" class="itxt" disabled="disabled" value="群账号：${requestScope.findGroup.groupName}"><%--群名称--%>
        <br><br>&nbsp;&nbsp;&nbsp;&nbsp;
        <i class="iconfont icon-qunzhu" id="iconid" name="weixin"></i>&nbsp;
        <input type="text" style="color: darkviolet" name="name" id="name" class="itxt" disabled="disabled" value="群主：${requestScope.GroupTopProple.name}"><%--群名称--%>
        <br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <span>群简介</span>
        <textarea disabled="disabled" class="textArea" id="text" placeholder="${requestScope.findGroup.groupAnnounce}"></textarea>
        <%--群简介--%>
        <%--判断--%>
        <c:if test="${requestScope.userSearchGroup == 'notYourFriend'}">
            <input type="button" value="添加群聊" class="button_1" id="button_1" name="button_1">
            <span style="color: green" id="tip"></span>
        </c:if>
        <c:if test="${requestScope.userSearchGroup != 'notYourFriend'}">
            <br><br><br><span style="color: red" >${requestScope.userSearchGroup}</span>
        </c:if>
    </div>
</body>
</html>
