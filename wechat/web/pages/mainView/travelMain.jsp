<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/21
  Time: 1:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>游客主界面</title>
    <link type="text/css" rel="stylesheet" href="static/css/travelMain.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2551021_zy3mhzrkvh.css"/>
    <script>
        var friendName;
        $(function (){
            $.ajax({
                url:"travelServlet?method=getTravelFriends",
                success: function (date){
                    if(date == "0"){
                        alert("没有好友，请添加");
                    }else{
                        var allfriend = JSON.parse(date);
                        for(var i = 0; i < allfriend.length; i++){
                            //展示到屏幕上
                            var friendName = allfriend[i].friendName;
                            var str = "<span onclick='showFriend(this)' id='"+ friendName +"'>"+ friendName +"</span><br>";
                            $("#userFriend").append(str);
                        }
                    }
                }
            })

            //拉黑好友
            $("#addToBlacklist").click(function (){
                $.post("travelServlet?method=addToBlackList&friendName="+friendName, function (date){
                    if(date == "0"){
                        alert("找不到该好友")
                    }else if(date == "2"){
                        alert("你已经拉黑过一次该好友")
                    }else{
                        alert("已拉黑")
                        document.getElementById("friends").src = "pages/others/travelFindFriend.jsp";
                    }
                })
            });

            //取消拉黑好友
            $("#notAddToBlacklist").click(function (){
                $.post("travelServlet?method=notAddToBlacklist&friendName="+friendName, function (date){
                    if(date == "0"){
                        alert("找不到该好友")
                    }else if(date == "2"){
                        alert("你没有拉黑该好友")
                    }else{
                        alert("已解除拉黑")
                        document.getElementById("friends").src = "pages/others/travelFindFriend.jsp";
                    }
                })
            })

            //添加好友
            $("#addFriend").click(function (){
                var friendAccount = $("#friendAccount").val();
                $.post("travelServlet?method=trivelFindFriend&friendAccount="+friendAccount, function (date){
                    if(date == "1"){
                        alert("已请求添加好友，等待对方同意");
                    }
                    if(date == "0"){
                        alert("请输入账号");
                    }
                    if(date == "2"){
                        alert("没有找到该用户")
                    }
                    if(date == "3"){
                        alert("正在等待对方同意添加")
                    }
                })
            })

            //删除好友
            $("#deleteFriend").click(function (){
                if(friendName == undefined || friendName==""){
                    alert("你还没有选择好友")
                }else{
                    $.post("travelServlet?method=triveldeleteFriend&friendName="+friendName, function (date){
                        if(date == "1"){
                            alert("已删除对方");
                        }
                    })
                }
            });

            //点击查找好友
            $("#findUser").click(function (){
                //获取输入的消息
                var friendAccount = $("#friendAccount").val();
                $.post("travelServlet?method=findFriend&friendName="+friendAccount, function (date){
                     if(date == "0"){
                         alert("你还没有输入账号");
                     }else if(date == "1"){
                         alert("没找到该用户")
                     }else{
                         document.getElementById("friends").src = "pages/others/travelFindFriend.jsp";
                     }
                });
            });

            //点击进入聊天室
            $("#intoChatRoom").click(function (){
                window.open("userChatServlet?method=travelIntoChat");
            })

        });

        //点击好友展示
        function showFriend(friend){
            var name = friend.id;
            friendName = name;
            $.post("travelServlet?method=getFriend&friendName="+name, function (date){
                if(date == "0"){
                    alert("没有找到该好友,程序异常")
                }else{
                    document.getElementById("friends").src = "pages/others/travelFindFriend.jsp";
                }
            });
        }
    </script>
</head>
<body>
<div class="content">
    <div class="tit">
        <h1 style="color:green">欢迎游客${sessionScope.loginUser.name}登陆</h1>
        <h3>登陆时间:${sessionScope.travelLoginTime}</h3>
    </div>
    <div class="userFriend" id="userFriend"></div>
    <iframe src="" class="friend" id="friends"></iframe><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" value="拉黑" id="addToBlacklist">
    <input type="button" value="取消拉黑" id="notAddToBlacklist">
    <input type="text" placeholder="输入用户账号" id="friendAccount"/>
    <input type="button" value="查找" id="findUser">
    <input type="button" value="添加好友" id="addFriend">
    <input type="button" value="删除好友" id="deleteFriend">
    <input type="button" value="进入好友聊天" id="intoChatRoom">
</div>
</body>
</html>
