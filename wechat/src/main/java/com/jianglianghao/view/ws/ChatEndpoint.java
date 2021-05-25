package com.jianglianghao.view.ws;

import com.jianglianghao.bean.Message;
import com.jianglianghao.controller.UserChatController;
import com.jianglianghao.entity.User;
import com.jianglianghao.entity.UserChat;
import com.jianglianghao.util.CommonUtil;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;
import sun.security.util.Length;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/178:59
 */

@ServerEndpoint(value= "/chat", configurator = GetHttpSessionConfigurator.class)
public class ChatEndpoint {

    //用来存储每一个客户端对象对于的ChatEndpoint对象,线程安全
    private static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //声明session对象，通过该对象可以发送消息给指定的用户,不是静态，每一个ChatEndpoint都需要一个session
    private Session session;

    //声明一个httpsession对象，我们之前在httpsession中存储的用户名
    private HttpSession httpSession;
    //存储img
    private byte[] b = null;

    @OnOpen
    //连接建立适合执行
    public void onOpen(Session session, EndpointConfig config){
        //将局部session对象赋值给成员session，标识唯一的用户
        this.session = session;
        //获取httpsession
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        //赋给成员的httpsession
        this.httpSession = httpSession;
        //从httpsession对象中获取用户名
        User user = (User) httpSession.getAttribute("loginUser");
        String username = user.getName();
        //将当前对象存储到容器里面，online证明了在线
        onlineUsers.put(username, this);
        //将当前在线用户的所有的用户名推送给所有的客户端
        //1. 获取消息,系统消息格式：true, null, ["name1", "name2", .....]
        String message = CommonUtil.getMessage(true, null, getNames() , null, null);
        //2. 调用方法进行系统消息的推送
        broadcastAllUsers(message);
    }

    //推送系统消息
    private void broadcastAllUsers(String message){
        //要将该消息推送给所有的客户端，遍历
        try {
            Set<String> strings = onlineUsers.keySet();
            for (String name : strings) {
                ChatEndpoint chatEndpoint = onlineUsers.get(name);
                //每一个chatEndpoint都对应了一个session对象
                synchronized (this.session) {
                    chatEndpoint.session.getBasicRemote().sendText(message);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取在线的所有用户
    private Set<String> getNames() {
        return onlineUsers.keySet();
    }

    @OnMessage
    //接收到客户端发送的数据时候被调用
    public void onMessage(String message, Session session) throws Exception{
        try {
            //接收到数据
            ObjectMapper objectMapper = new ObjectMapper();
            //获取消息，Message形式
            Message message1 = objectMapper.readValue(message, Message.class);
            //获取发送给谁
            String toName = message1.getToName();
            //获取发送的内容
            String msg = message1.getMessage();
            //转换内容，把内容中的'替换为"
            String replace = msg.replace("'", "\"");
            //存入数据库
            UserChat userChat = new UserChat(0, ((User) httpSession.getAttribute("loginUser")).getName(), toName, replace, "text");
            new UserChatController().sendContent(userChat);

            //获取本session的对象名字
            String username = ((User) httpSession.getAttribute("loginUser")).getName();
            //获取session的头像路径
            String headProtrait = message1.getHead();
            //通过指定格式推送给用户
            String text = CommonUtil.getMessage(false, username, msg, "text", headProtrait);
            //发送给对象
            boolean b = onlineUsers.containsKey(toName);
            if(b == true){
                //用户上线了
                synchronized (session) {
                    onlineUsers.get(toName).session.getBasicRemote().sendText(text);
                }
            }else{
                //用户没上线，存入数据库，以后方便读取
                System.out.println("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnMessage
    //专门用来接受发过来的文件数据,前端解码后是byte[]数组
    public void onMessage(byte[] inputStream, Session session, boolean last) throws Exception{
        //定义一个容器，把前台传过来的session存起来
        if(last == false){
            //没传完,b不断追加，追加
            b = ArrayUtils.addAll(b, inputStream);
        }else{
            //添加最后一次字节
            b = ArrayUtils.addAll(b, inputStream);
            //传完了
            ByteBuffer bb = ByteBuffer.wrap(b);
            //发送消息
            this.broadcastbytes(bb);
            b = null;
        }
    }


    //推送系统消息,这个是发送二进制数据的方法
    private void broadcastbytes(ByteBuffer message){
        //要将该消息推送给所有的客户端，遍历
        try {
            Set<String> strings = onlineUsers.keySet();
            for (String name : strings) {
                //消息不发送给自己
                if(!name.equals(((User)httpSession.getAttribute("loginUser")).getName())){
                    ChatEndpoint chatEndpoint = onlineUsers.get(name);
                    //每一个chatEndpoint都对应了一个session对象
                    synchronized (this.session) {
                        chatEndpoint.session.getBasicRemote().sendBinary(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    //连接关闭被调用
    public void onClose(Session session){
        User loginUser = (User)httpSession.getAttribute("loginUser");
        //获取下线的用户
        String username = loginUser.getName();
        onlineUsers.remove(username);
        //获取推送的消息
        String message = CommonUtil.getMessage(true, null, getNames(), "downline", null);
        broadcastAllUsers(message);
        //关闭了，就删除了。
    }


}
