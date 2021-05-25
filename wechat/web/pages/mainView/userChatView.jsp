<%--
  Created by IntelliJ IDEA.
  User: jianglianghao
  Date: 2021/5/16
  Time: 12:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/pages/common/head.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="static/lib/jquery-CustomScrollbar/css/jquery.mCustomScrollbar.min.css"/>
    <link rel="stylesheet" href="static/lib/jquery-emoji/css/jquery.emoji.css"/>
    <link type="text/css" rel="stylesheet" href="static/css/userChatView.css"/>
    <link type="text/css" rel="stylesheet" href="//at.alicdn.com/t/font_2551021_zy3mhzrkvh.css"/>
    <title>用户聊天界面</title>
    <script>

        //一上来先请求获取用户名
        var username;
        //和谁聊天
        var toName;
        //创建websocket对象
        var ws;
        var head;
        var toNameNickName;
        var fromName;
        var fileDate;

        //建立服务功能,执行以下方法
        function ws_connect() {
            //获取连接
            if ('WebSocket' in window) {
                ws = new WebSocket("ws://localhost:8080/wechat/chat");
            } else if ('MozWebSocket' in window) {
                ws = new MozWebSocket("ws://localhost:8080/wechat/chat");
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            ws.onopen = function () {
                //显示在线信息
                $("#username").html("用户：" + username + "(" + "<span style=' color: green'>在线</span>" + ")")
            }

            ws.onclose = function () {
                //显示离线消息
                //$("#username").html("用户：" + username + "<span style='float: bottom; color: red'>离线</span>")
            };

            ws.onmessage = function (message) {
                var dateStr = message.data
                if (typeof (dateStr) == "string") {
                    //证明是文本消息，不是流消息
                    var res = JSON.parse(dateStr);
                    var head = res.head;
                    fromName = res.fromName;
                    //判断是否是系统消息
                    if (res.isSystem) {
                        var names = res.message;
                        //系统广播展示
                        var broadcastListSte = $("#broadcastList").html();
                        for (var name of names) {
                            if (name != username) {
                                //上线消息，自己上线不需要提示给自己看
                                broadcastListSte += "<li style='color: green; font-family:宋体'>你的好友" + name + "已上线</li>";
                            }
                            //渲染系统广播消息
                            $("#broadcastList").html(broadcastListSte);
                        }
                    } else {
                        //不是系统消息，将服务端推送的消息展示
                        var content = res.message;
                        var str = "<div class='on_left'>"+ fromName +"<div class='on_right_content'>" + content + "</div></div>";
                        //判断，加入是张三和王五聊天，那么张三的方面fromname=王五，toname=王五，防止加入李四发了一个消息导致的出错
                        if (toName == res.fromName) {
                            $("#chatArea").append(str);
                        }
                        var charDate = sessionStorage.getItem(toName);
                        if (charDate != null) {
                            str = charDate + str;
                        }
                        sessionStorage.setItem(toName, str);
                    }
                } else {
                    //流消息
                    var reader = new FileReader();
                    reader.onload = function loaded(evt) {
                        var url = evt.target.result;
                        var str = "<div class='on_right'>"+fromName+"<img style='width: 40px; height: 40px' class='' src='" + url + "'>" + "</img></img></div>";
                        $("#chatArea").append(str);
                        var charDate = sessionStorage.getItem(toName);
                        if (charDate != null) {
                            str = charDate + str;
                        }
                        sessionStorage.setItem(toName, str);
                    }
                    reader.readAsDataURL(message.data);
                };
            }
        };


        //发送消息
        function ws_senMsg() {
            var content = $("#printContent").html();
            //设置输入框为空
            $("#printContent").html("");

            //将消息展示在上面
            var str = "<div class='on_right'><div class='on_left_content'>" + content +":"+ username + "</div></div><br><br>";
            //拼接字符串
            var json = {"toName": toName, "message": content, "head": head};

            $("#chatArea").append(str);
            var charDate = sessionStorage.getItem(toName);
            if (charDate != null) {
                str = charDate + str;
            }
            sessionStorage.setItem(toName, str);
            //发送给服务端
            ws.send(JSON.stringify(json));
        }

        //发送文件
        function sendFile(f) {
            var file = f.files[0];//获取file组件中的文件
            var file1 = f.files[0];//获取file组件中的文件
            //判断字节是多少，大于10m的不能上传
            var size = file1.size;
            if (size > 1024 * 1024 * 10) {
                //大于10m的文件不能上传
                alert("请选择小于10m的文件")
                return;
            }
            var fileName = file.name;
            var regex1 = /.+(.JPEG|.jpeg|.JPG|.jpg|.png)$/;
            if (file != false && regex1.test(fileName)) {
                //是图片
                //解码
                var fr = new FileReader();
                var fr1 = new FileReader();
                var formData = new FormData();
                formData.append('file', f.files[0]);  //添加图片信息的参数
                //使用ajax上传
                $.ajax({
                    url: "fileServlet?method=userChatWithFriendAddPicture&friendName=" + toName,
                    type: "post",
                    dataType: "json",
                    cache: false,
                    data: formData,
                    processData: false,// 不处理数据
                    contentType: false, // 不设置内容类型
                    success: function (data) {

                    },
                    async: true
                })
                //先不读为Buffer
                fr.readAsDataURL(file);
                fr.onload = function (evt) {
                    //拼接
                    var str = "<div class='on_right'><img style='width: 40px; height: 40px' class='on_right_content1' src=" + evt.target.result + ">"+ ":" + username +"</img></div></br></br>";
                    $("#chatArea").append(str);
                    //缓存
                    var charDate = sessionStorage.getItem(toName);
                    if (charDate != null) {
                        str = charDate + str;
                    }
                    sessionStorage.setItem(toName, str);
                }
                fr1.readAsArrayBuffer(file1);
                fr1.onload = function (evt) {
                    var blob = evt.target.result;
                    ws.send(blob);
                }
            } else {
                //不是图片
                //解码
                var fr = new FileReader();
                var fr1 = new FileReader();
                //先不读为Buffer
                fr.readAsDataURL(file);
                //传送到后台获取路径
                var formData = new FormData();
                formData.append('file', f.files[0]);  //添加图片信息的参数
                //使用ajax上传
                $.ajax({
                    url: "fileServlet?method=userChatWithFriendAddFile&friendName=" + toName,
                    type: "post",
                    dataType: "json",
                    cache: false,
                    data: formData,
                    processData: false,// 不处理数据
                    contentType: false, // 不设置内容类型
                    success: function (data) {
                        fileDate = data;
                        var str = "<div class='on_right4'></img><a href='fileServlet?method=download&filename=" + fileDate + "' class='files1'>" + fileName + "</a>" + ":" + username + "</div></br></br>";
                        var content = "<a class='files1' href='fileServlet?method=downloadGroupFile&filename=" + fileDate + "'>" + fileName + "</a>";
                        var json = {"toName": toName, "message": content};
                        //拼接
                        $("#chatArea").append(str);
                        //缓存
                        var charDate = sessionStorage.getItem(toName);
                        if (charDate != null) {
                            str = charDate + str;
                        }
                        sessionStorage.setItem(toName, str);
                        //发送div标签
                        ws.send(JSON.stringify(json));
                    },
                    async: true
                });

            }
        }

        //点击人物
        function showChat(friendName) {
            $("#printContent").attr("contentEditable", true);
            toName = friendName.id;
            //获取好友的昵称
            $.ajax({
                url: "userChatServlet?method=getNickName&friendname=" + toName,
                success: function (date) {
                    //赋值
                    toNameNickName = date;
                },
                async: false
            });
            //切换不同的人就要清空聊天区
            $("#chatArea").html("");
            $("#chatToWho").html("正在和" + toNameNickName + "(" + toName + ")" + "聊天").css("color", "green");
            var charDate = sessionStorage.getItem(toName);
            if (charDate != null) {
                //页面缓存聊天记录
                $("#chatArea").html(charDate);
            }
        }


        $(function () {
            //ajax获取用户名
            $.ajax({
                url: "userChatServlet?method=getName",
                success: function (date) {
                    //赋值
                    username = date;
                    $("#username").html("用户：" + username + "(" + "<span style='color: green'>在线</span>" + ")");
                },
                async: false
            });
            //ajax获取用户头像
            $.ajax({
                url: "userChatServlet?method=getHead",
                success: function (date) {
                    //赋值
                    head = date;
                },
                async: false
            });

            //执行websocket——connect方法
            ws_connect();
            //点击发送消息
            $("#sendMsg").click(function () {
                ws_senMsg();
            });

            //点击查看历史记录
            $("#showRecord").click(function () {
                $.post("userChatServlet?method=saveFriendName&friendName=" + toName, function (date) {
                    if (date == "0") {
                        alert("还没有选择好友");
                    } else {
                        window.open("pages/others/userChattingRecord.jsp");
                    }
                });
            })
        });
    </script>
</head>
<body>
<div class="content_1">
    <div class="managerHead_1">
        <span id="username"></span><br>
        <span style="color: green" id="chatToWho"></span>
    </div>
    <div class="chatArea" id="chatArea" style="overflow: auto;">
        <%--        <div class="on_left"><img class="heading" id="heading" src=""><div class="on_left_content"></div></div>--%>
        <%--        <div class="on_right"><img class="heading1" id="heading1" src=""><div class="on_right_content"></div></div>--%>
    </div>
    <div class="middle"><br>
        <button id="showRecord" name="showRecord" class="showRecord">查看历史记录</button>
        <i class="iconfont  icon-liaotian-copy" id="icon-liaotian-copy" name="icon-liaotian-copy"></i>
        <i class="iconfont  icon-Ovalx" id="icon-Ovalx" name="icon-Ovalx"></i>
        <i class="iconfont  icon-biaoqingbao" id="icon-biaoqingbao" name="icon-biaoqingbao"></i>
        <a class="iconfont  icon-wenjian" id="icon-wenjian" name="icon-wenjian" title="文件"><label
                for="file"></label><input type="file" id="file" name="file" style="opacity: 0" class="file"
                                          onchange="sendFile(this)"></a>
        <i class="iconfont  icon-tianjia" id="icon-tianjia" name="icon-tianjia"></i>
        <button id="sendMsg" name="sendMsg" class="sendMsg">发送消息</button>
    </div>
    <div class="buttom">
        <%--富文本框--%>
        <div contenteditable="false" class="printContent" disabled id="printContent" placeholder="在此输入要发送的消息"></div>
    </div>
    <%--展示用户区域--%>
    <div class="right">
        <div class="friendList" style="border: black solid 1px; color: #00dbde">好友列表</div>
        <ul class="allFriend" style="border: black solid 1px">
            <c:if test="${sessionScope.findAllFriendsInFriendsChat.size() > 0 }">
                <c:forEach begin="0" end="${sessionScope.findAllFriendsInFriendsChat.size()-1}" var="i">
                    <span style="font-size: 30px" onclick="showChat(this)"
                          id="${sessionScope.findAllFriendsInFriendsChat.get(i).friendName}">${sessionScope.findAllFriendsInFriendsChat.get(i).friendName}</span><br>
                </c:forEach>
            </c:if>
        </ul>
        <div class="radio" style="border: black solid 1px; color: #00dbde">广播系统</div>
        <ul class="allMessages" style="border: black solid 1px" id="broadcastList">
            <li style="color: #9d9d9d; font-family:宋体"></li>
        </ul>
    </div>
</div>
<%--引入表情js--%>
<script src="static/lib/jquery-CustomScrollbar/script/jquery.mCustomScrollbar.min.js"></script>
<script src="static/lib/jquery-emoji/js/jquery.emoji.min.js"></script>
<script src="static/script/index.js"></script>
</body>
</html>
