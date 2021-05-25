<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/22
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/userChattingRecord.css"/>
    <title>群聊记录</title>
    <script>
        $(function (){
            //使用ajax查找记录
            $.ajax({
                url:"groupChatServlet?method=findGroupRecord",
                success:function (date){
                    if(date == "0"){
                        alert("没有群聊记录")
                    }else{
                        var records = JSON.parse(date);
                        for(var i = 0; i < records.length; i++){
                            var userName = records[i].userName;
                            var content = records[i].msg;
                            var type = records[i].msgType;
                            if(type != "picture"){
                                //不是图片
                                var str = "<span>"+ userName +"</span><div class='content_left'>"+ content +"</div>"
                                $("#allChat").append(str);
                            }else{
                                //是图片
                                var img = "<img src='"+ content +"' width='40px' height='40px'/>"
                                var str = "<span>"+ userName +"</span><div class='content_left'>"+ img +"</div>"
                                $("#allChat").append(str);
                            }
                        }
                    }
                }
            })

            $("#clearall").click(function (){
                $.post("groupChatServlet?method=clearAll", function (date){
                    if(date == "1"){
                        alert("已删除")
                    }else{
                        alert("程序异常，请联系2429890953@qq.com解决");
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
