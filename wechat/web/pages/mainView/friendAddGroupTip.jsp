<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/8
  Time: 22:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/friendAddTip.css"/>
    <title>群聊申请添加提示</title>
    <script>

        //初始化
        $(function () {
            $("#btn_1").click(function (){
                $.post("userAndGroupServlet?method=userAgreeToInGroup", function (date){
                    if(date == "1"){
                        alert("已同意");
                    }else{
                        alert("网站异常，请联系管理员2429890953@qq.com反馈");
                    }
                });
            });

            $.ajax({
                url:"userAndGroupServlet?method=findAllGroupAddMsg",
                success: function (date){
                    var message = JSON.parse(date);
                    for(var i = 0; i < message.length; i ++){
                        var content = message[i].userAccount+":"+message[i].groupName;
                        var str = "<span onclick='checkMsg(this)' id='"+ content +"'>"+ content +"</span></br>";
                        $("#allUserAdd").append(str);
                    }
                }
            })

        });

        function checkMsg(group){
            //获取信息
            var msg = group.id;
            $.post("userAndFriendServlet?method=findUserByAccountInTipJsp&account=" + msg, function (date){
                if(date == "1"){
                        document.getElementById("iframe_1").src = "pages/others/userMessage.jsp";
                }
            })
        }
    </script>
</head>
<body>
<div id="content">
    <div class="form_1">
        <h1 style="color: #a94442">好友或群聊申请通知</h1>
    </div>
    <%--中部--%>
    <div class="form_2">
        <%--添加表格--%>
        <ul class="fanshi1" id="allUserAdd">

        </ul>
        <iframe class="iframe_1" id="iframe_1" name="iframe_1">
        </iframe>
        <input type="submit" id="btn_1" name="btn_1" value="通过" class="btn"><span id="msg_1" style="color: red"></span>
    </div>
</div>
</body>
</html>
