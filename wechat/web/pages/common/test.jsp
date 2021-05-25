<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/7
  Time: 1:40
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<html>
<head>
    <title>Title</title>
    <%@ page  isELIgnored="false"%>
    <style>
        .fanshi{
            position: absolute;
            background-color: white;
            width: 250px;
            height: 617px;
            margin-left: 4%;
            margin-top: 0px;
            overflow: auto;
            overflow-y: auto;
        }
    </style>
</head>
<body>
<ul class="fanshi">
    <%-- 动态输入好友列表内容 --%>
    <c:forEach begin="0" end="100" var="i">
        <table align="center" border="1" width = "250" height = "100" cellspacing="0">
            <tr>
                <td align="center"><b>1.1</b></td>
            </tr>
        </table>
    </c:forEach>
</ul>
</body>
</html>
