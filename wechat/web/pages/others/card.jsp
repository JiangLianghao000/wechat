<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/23
  Time: 1:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="static/css/card.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2519156_c2jcn9kjoyn.css"/>
    <title>好友卡片</title>
    <script>
        var friendName;
        $(function (){
           $.post("userAndFriendServlet?method=addFriendsInCardView", function (data){
                if(data == "0"){
                    alert("你还没有好友")
                }else{
                    var friends = JSON.parse(data);
                    for(var i = 0; i < friends.length; i ++){
                        var friend = friends[i].friendName;
                        $("#selectSendPeople").append("<option value='"+friend+"'>"+friend+"</option>");
                        $("#selectBeSendPeople").append("<option value='"+friend+"'>"+friend+"</option>");
                    }
                }
           });

           //获取所有卡片
            $.ajax({
                url:"userAndFriendServlet?method=findAllCards",
                dataType:'json',
                success:function (date){
                    var cards = date;
                    for(var i = 0; i < cards.length; i ++){
                        var friendName = cards[i].cardPeople;
                        var content = "<span>"+ "第" + (i+1) + "个卡片: " +"</span><span onclick='addPeople(this)' id='"+ friendName +"'>"+ friendName +"</span></br>";
                        $("#allCards").append(content);
                    }
                }
            })

           $("#send").click(function (){
               //发送卡片
               //发送的朋友
               var sendPeople = $("#selectSendPeople").val();
               //被发送的朋友
               var beSendPeople = $("#selectBeSendPeople").val();

               //发送卡片
               $.post("userAndFriendServlet?method=sendCard&sendPeople="+sendPeople+"&beSendPeople="+beSendPeople, function (date){
                    if(date == "-1"){
                        alert("程序异常");
                    }
                    if(date == "1"){
                        alert("两个选择不可以相同");
                    }
                    if(date == "2"){
                        alert("选择的两人已经是好友关系");
                    }
                    if(date == "3"){
                        alert("已发送");
                    }
               });
           })

            $("#agree").click(function (){
               var agreeOrNot = confirm("你确定要用卡片添加好友吗");
               if(agreeOrNot == true){
                   $.post("userAndFriendServlet?method=addFriendByUsingCard&friendName="+friendName, function (date){
                       if(date == "0"){
                           alert("找不到该好友")
                       }else{
                           window.location.href="pages/others/card.jsp";
                       }
                   });
               }
            });
        });

        function addPeople(e){
           friendName = e.id;
           $("#agree").removeAttr("disabled");
        };

    </script>
</head>
<body>
<div class="content">
    <h1 style="color: green">好友卡片界面</h1>
    <span>选择接受人</span><select id="selectSendPeople" style="width: 200px" name="selectSendPeople"></select><br><br>
    <span>选择要发送哪位好友</span><select id="selectBeSendPeople" style="width: 200px" name="selectBeSendPeople"></select>
    <button class="send" name="send" id="send">发送卡片</button><br><br>
    <h3 style="color: green">收到的卡片</h3>
    <ul id="allCards" name="allCards" class="allCards"></ul>
    <input type="button" class="agree" id="agree" name="agree" value="使用卡片" disabled>

</div>
</body>
</html>
