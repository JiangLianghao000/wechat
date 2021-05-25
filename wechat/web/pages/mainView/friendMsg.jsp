<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/6
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>好友信息界面</title>
    <style type="text/css">
        .friendMsg{
            position: absolute;
            margin-left: 45%;
            margin-top:-100px;
        }

        .addFriend{
            position: absolute;
            margin-left: 44%;
            margin-top:-100px;
            border: 0; /* 取消按钮的边界 */
            width: 150px; /* 设置合适的按钮的长和宽 */
            height: 30px;
            font-size: 18px; /* 修改按钮文字的大小 */
            color: white; /* 修改按钮上文字的颜色 */
            border-radius: 25px; /* 将按钮的左右边框设置为圆弧 */
            background-image: linear-gradient(to right, #00dbde 0%, #fc00ff 100%); /* 为按钮增加渐变颜色 */
            cursor: pointer; /* 鼠标移入按钮范围时出现手势 */
        }
    </style>
    <script>
        //点击添加好友,用这个方法因为有元素为加载完成的，用functiom不能生效
        window.onload = function (){
            $("#addFriend").on("click", function (){
                var r = confirm("你确定要添加对方为好友吗");
                if(r == true){
                    //对方要受到好友请求，调用数据库，设置state为待添加状态
                    $.post("userAndFriendServlet?method=addFriend", function (date){
                        if(date == "1"){
                            alert("你已添加对方为好友,等待对方同意")
                        }
                    });
                }
            });
        };
    </script>
</head>
<body>
    <%--查到好友后添加进findUser的attribute域中--%>
    <%@ include file="../common/msg.jsp" %>
    <%--判断，如果是有数据了，并且不是你的好友，才可以选择去添加--%>
    <c:choose>
        <c:when test="${sessionScope.userSearchFriend=='notYourFriend'}">
            <%--这时候表明不是好友,有按钮可以添加--%>
            <input type="button" class="addFriend" id="addFriend" name="addFriend" value="添加好友"/>
        </c:when>
        <c:when test="${!(sessionScope.userSearchFriend=='notYourFriend')}">
            <span style="color: red" class="friendMsg" id="friendMsg" name="friendMsg">${sessionScope.userSearchFriend}</span>
        </c:when>
    </c:choose>
</body>
</html>
