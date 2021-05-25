
<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/15
  Time: 1:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/modifyGroupMsg.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2537798_l7w4vbydmxj.css"/>
    <title>修改群聊信息：群主专用</title>
    <script>
        function show(f) {
            $("#btn").removeAttr("disabled");
            var reader = new FileReader();//创建文件读取对象
            var files = f.files[0];//获取file组件中的文件
            reader.readAsDataURL(files);//文件读取装换为base64类型
            reader.onloadend = function (e) {
                //加载完毕之后获取结果赋值给img
                document.getElementById("showimg").src = this.result;
            }
        };

        //如果是群主，可以修改简介和群名字和群昵称
        $(function (){
            if(${sessionScope.findUserByAccountInFriendTipJsp.userId eq sessionScope.loginUser.id}){
                $("#name").removeAttr("disabled");
                $("#textArea_3").removeAttr("disabled");
            }

            $("#submit").click(function (){
                $("#msg").html("");
                var name = $("#name").val();
                var nickName = $("#qunnicheng").val();
                var text = $("#textArea_3").val();
                var userNickName = $("#nicheng").val();
                $.post("userAndGroupServlet?method=mofidyGroupMsg&name="+name+"&nickName="+nickName+"&text="+text+"&userNickName="+userNickName,function (date){
                    if(date=="1"){
                        $("#msg").html("输入和含有敏感词汇的群简介").css("color", "red");
                    }else if(date == "2"){
                        $("#msg").html("输入的群名不合法").css("color", "red");
                    }else if(date == "3"){
                        $("#msg").html("输入的群昵称不合法").css("color", "red");
                    }else if(date == "4"){
                        $("#msg").html("输入的在群的昵称不合法").css("color", "red");
                    }else if(date == "5"){
                        $("#msg").html("输入的群名已经存在了，请重新输入一个").css("color", "red");
                    }else if(date == "6"){
                        alert("已修改");
                        location = location;
                    }else if(date == "7"){
                        $("#msg").html("输入的群昵称不合法").css("color", "red");
                    }else if(date == "8"){
                        $("#msg").html("输入的在群的昵称不合法").css("color", "red");
                    }else{
                        alert("已修改");
                        location = location;
                    }
                });
            });


        });
    </script>
</head>
<body>
<div id="content">
    <div class="tit">
        <h1 style="color:white">修改群聊信息</h1>
        <span class="errorMsg" style="color: #ff0000" id="msg" name="msg"></span>
    </div>
    <form action="fileServlet?method=setGroupHead"  method="post"  enctype="multipart/form-data">
        <input type="file" id="headimg" name="headimg" class="file1" accept="image/png, image/jpeg, image/gif, image/jpg" onchange="show(this)">
        <img id="showimg" name="showimg" class="head_portrait" src="${sessionScope.findUserByAccountInFriendTipJsp.groupHeadportrait}">
        <input type="submit" value="设置" disabled="disabled" class="setHead" id="btn">
    </form>
        <br><br>
        <i class="iconfont icon-qunmingpian" id="icon-qunmingpian" name="icon-qunmingpian"></i>
        <input type="text" id="name" name="name" class="itxt" value="${sessionScope.findUserByAccountInFriendTipJsp.groupName}" disabled="disabled">
        <br/><br/><br/>

        <i class="iconfont icon-qunzhanghao" id="icon-qunzhanghao" name="icon-qunzhanghao"></i>
        <label id="account" name="account" class="labels">群账号: ${sessionScope.findUserByAccountInFriendTipJsp.groupAccount}</label>
        <br/><br/><br/>

        <i class="iconfont icon-qunzhu" id="icon-qunzhu" name="icon-qunzhu"></i>
        <label type="text" name="qunzhu" id="qunzhu" class="labels">群主： ${sessionScope.groupMainPeople}</label>
        <br/><br/><br/>

        <i class="iconfont icon-qunnicheng" id="qunnichen" name="icon-qunnicheng" ></i>
        <input type="text" id="qunnicheng" name="qunnicheng" class="itxt_1" placeholder="群昵称:">
        <br>
        <i class="iconfont icon-qunnicheng" id="icon-qunnicheng" name="icon-qunnicheng" ></i>
        <input type="text" id="nicheng" name="nicheng" class="itxt_1" placeholder="设置用户在群的昵称">
        <br>
        <i class="iconfont icon-jianjie" id="icon-jianjie" name="icon-jianjie"></i><br>
        <textarea disabled="disabled" class="textArea_3" id="textArea_3" name="textArea_3" placeholder="简介：${sessionScope.findUserByAccountInFriendTipJsp.groupAnnounce}"></textarea>
        <br/><br/>
        <input type="submit" name="submit" id="submit" class="button_css" value="修改信息">
</div>
</body>
</html>
