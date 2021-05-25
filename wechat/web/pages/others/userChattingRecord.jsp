<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/20
  Time: 0:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/userChattingRecord.css"/>
    <title>用户聊天记录</title>
    <script>
        $(function (){
            $.ajax({
                url: "userChatServlet?method=getFriendChat",
                success: function (date) {
                    var msg = JSON.parse(date);
                    for(var i = 0; i < msg.length; i++){
                        var type = msg[i].msgType;
                        var content = msg[i].content;
                        var userName = msg[i].userName;
                        if(type != "picture"){
                            var str = "<span>"+ userName +"</span><div class='content_left'>"+ content +"</div>"
                            $("#allChat").append(str);
                        }else{
                            //图片路径
                            var img = "<img src='"+ content +"' width='40px' height='40px'/>"
                            var str = "<span>"+ userName +"</span><div class='content_left'>"+ img +"</div>"
                            $("#allChat").append(str);
                        }
                    }
                },
                async: false
            });

            $("#clearall").click(function (){
                //删除所有聊天记录
                $.post("userChatServlet?method=deleteAllChatMsg&friendName=${sessionScope.userFindFriendNameInCharRoom}", function (date){
                    if(date == "0"){
                        alert("没有选择好友，无法删除信息");
                    }else{
                        alert("已删除聊天信息");
                    }
                });
            });
        })
    </script>
</head>
<body>
<div class="content">
    <div class="allChat" id="allChat">

    </div>
    <button id="clearall" name="clearall" class="clearall">清除所有聊天记录</button>
</div>
</body>
</html>
