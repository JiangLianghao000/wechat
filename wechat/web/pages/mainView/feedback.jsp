<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/5
  Time: 18:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/feedback.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2519156_doim610x4m7.css"/>
    <title>反馈窗口</title>
    <script>
        $(function (){
            $("#managerAccount").change(function (){
                $("#msg_3").html("");
                $.post("userMSGServlet?method=checkManagerAccount&managerAccount=" + $("#managerAccount").val(), function (date){
                    if(date == "0"){
                        $("#msg").html("输入账号为空").css("color", "red")
                    }
                    if(date == "1"){
                        $("#msg").html("找不到用户").css("color", "red")
                    }
                    if(date == "2"){
                        $("#msg").html("该用户不是管理员").css("color", "red")
                    }
                    if(date == "3"){
                        $("#msg").html("");
                    }
                });
            });

            $("#feedback").change(function (){
                $("#msg_3").html("");
                $.post("userMSGServlet?method=checkTextArea&feedback="+$("#feedback").val(), function (date){
                    if( date == "1"){
                        //证明有敏感词
                        $("#msg1").html("反馈信息含有敏感词").css("color", "red");
                    }
                    if(date == "0"){
                        $("#msg1").html("");
                    }
                    if(date == "2"){
                        $("#msg1").html("你还没有输入反馈信息，请确定").css("color", "red");
                    }
                    if(date == "3"){
                        $("#msg1").html("请不要输入'='号").css("color", "red");
                    }
                });
            });

            $("#btn_1").click(function (){

                //点击提交按键
                var test = confirm("你确定要提交吗？");
                if(test == true){
                    if($("#managerAccount").val()!="" && $("#feedback").val() != "") {
                        $.post("userMSGServlet?method=addMsgInFeedback&account=" + $("#managerAccount").val() + "&feedback=" + $("#feedback").val() +
                            "&msg1=" + $("#msg1").html() + "&msg=" + $("#msg").html(), function (date) {
                            if (date == "1") {
                                $("#msg_3").html("已通知对方").css("color", "green");
                            }
                            if (date == "0") {
                                $("#msg_3").html("反馈失败，程序出错，请联系管理员反馈信息").css("color", "red");
                            }
                            if(date == "error"){
                                $("#msg_3").html("请检测账号和反馈信息都合法再提交").css("color", "red");
                            }
                        });
                    }else{
                            $("#msg_3").html("请检测账号和反馈信息都合法再提交").css("color", "red");
                    }
                }
            });

        });

    </script>
</head>
<body>
    <div id="content">
        <h1 style="color: green">反馈信息</h1>
        <span id="msg_3" name="msg_3"></span>
        <br><br>
            请输入管理员账号：
            <input type="text" class="text_1" id="managerAccount" name="managerAccount">
            <br><br><br><br>
            请输入反馈信息：
            <textarea class="feedback" id="feedback" name="feedback"></textarea>
            <br>
            <input type="button" class="btn_1" id="btn_1" name="btn_1" value="反馈">
        <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <span id="msg"></span><br>
        <span id="msg1"></span>
    </div>
</body>
</html>
