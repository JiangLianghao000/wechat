//给ws绑定事件
ws.onopen = function () {
//在建立连接后需要做什么
//设置为在线状态
$("#username").html("用户：" + username + "(" + "<span style=' color: green'>在线</span>" + ")")
}

ws.onmessage = function (evt) {
//接受到服务端推送的消息后触发,获取服务端推送过来的消息
var dateStr = evt.data;
console.log( evt.data)
if(typeof(dateStr) == "string"){
//转化为json格式的字符串
var res = JSON.parse(dateStr);
//判断是否是系统消息
if (res.isSystem) {
var names = res.message;
var type = res.messageType;
//1. 系统广播展示
var broadcastListSte =  $("#broadcastList").html();
for (var name of names) {
if (name != username && type != "downline") {
//上线消息
broadcastListSte += "<li style='color: green; font-family:宋体'>你的好友" + name + "已上线</li>";
}
if (name != username && type == "downline") {
broadcastListSte += "<li style='color: red; font-family:宋体'>你的好友" + name + "已下线</li>";
}
}
//渲染好友消息
$("#broadcastList").html(broadcastListSte);
} else {
var res = JSON.parse(dateStr);
//不是系统消息，取出来头像路径
var headporait = res.head;
//判断是什么类型的消息，表情，表情包和基本消息
var type = res.messageType;
var content = res.message;

if (type == "text") {
//如果是纯文本，，拼接字符串显示到聊天框
var str = "<div class='on_left'><img class='heading' id='heading' src='" + headporait + "'" + "><div class='on_right_content'>" + res.message + "</div></div>";
//判断，加入是张三和王五聊天，那么张三的方面fromname=王五，toname=王五，防止加入李四发了一个消息导致的出错
if (toName == res.fromName) {
$("#chatArea").append(str);
}
//处理到浏览器的缓存中
var charData = sessionStorage.getItem(res.fromName);
if (charData != null) {
str = charData + str;
}
sessionStorage.setItem(res.fromName, str);
}
}
}else{
var reader = new FileReader();
reader.onload = function loaded(evt){
var url = evt.target.result;
var str = "<div class='on_right'><img class='heading' id='heading' src='" + head + "'" + "><img style='width: 40px; height: 40px' class='' src='" + url+ "'>" + "</img></img></div>";
$("#chatArea").append(str);
}
reader.readAsArrayBuffer(evt.data);
}
}

//关闭连接
ws.onclose = function () {
//显示离线消息
$("#username").html("用户：" + username + "<span style='float: bottom; color: red'>离线</span>")

}

//点击发送消息
$("#sendMsg").click(function () {
//获取文本消息
var content = $("#printContent").html();
//设置输入框为空
$("#printContent").html("");
//拼接字符串
var json = {"toName": toName, "message": content};
//发送给服务端
ws.send(JSON.stringify(json));
//将消息展示在上面
var str = "<div class='on_right'><img class='heading1' id='heading1' src='${sessionScope.loginUser.headProtrait}'><div class='on_left_content'>" + content + "</div></div>";
$("#chatArea").append(str);
//存储的消息拼接
var charData = sessionStorage.getItem(toName);
if (charData != null) {
str = charData + str;
}
sessionStorage.setItem(toName, str);
});
});


function sendFile(f){
var file = f.files[0];//获取file组件中的文件
if(file){
//解码
var fr = new FileReader();
fr.readAsArrayBuffer(file);
fr.onload = function (evt){
var blob = evt.target.result;
var str = "<div class='on_right'><img class='heading' id='heading' src='" + head + "'" + "><img style='width: 40px; height: 40px' class='on_right_content' src=" + blob+ "></img></img></div>";
$("#chatArea").append(str);
ws.send(blob);
}
$("#file").val("");
}else{
$("#file").val("");
return;
}
};
//点击好友进行赋值和展示
function showChat(name) {
$("#printContent").attr("contentEditable", true);
toName = name.id;
//切换不同的人就要清空聊天区
$("#chatArea").html("");
$("#chatToWho").html("正在和" + toName + "聊天").css("color", "green");
var charData = sessionStorage.getItem(toName);
if (charData != null) {
$("#chatArea").html(charData);
}