package com.jianglianghao.view.ws;

import com.jianglianghao.bean.Message;
import com.jianglianghao.controller.GroupChatController;
import com.jianglianghao.controller.UserAndGroupController;
import com.jianglianghao.controller.UserChatController;
import com.jianglianghao.entity.*;
import com.jianglianghao.util.CommonUtil;
import javafx.scene.control.Alert;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/2023:47
 */

@ServerEndpoint(value= "/groupChat", configurator = GetHttpSessionConfigurator.class)
public class GroupChatEndpoint {
    //用来存储每一个客户端对象对于的ChatEndpoint对象,线程安全
    private static Map<String, GroupChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //声明session对象，通过该对象可以发送消息给指定的用户,不是静态，每一个ChatEndpoint都需要一个session
    private Session session;

    //声明一个httpsession对象，我们之前在httpsession中存储的用户名
    private HttpSession httpSession;
    //存储img
    private byte[] b = null;
    //存储要查找的群聊
    private String group;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        try {
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
            //下面把上线的用户广播给所有的用户
            //广播消息，名字，所有用户
            String message = CommonUtil.getMessage(true, user.getName(), getNames() , null, null);
            //2. 调用方法进行系统消息的推送
            broadcastAllUsers(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void OnMessage(String message, Session session){
        try {
            //接收到数据
            ObjectMapper objectMapper = new ObjectMapper();
            //获取消息，Message形式
            Message message1 = objectMapper.readValue(message, Message.class);
            //获取发送给哪个群聊
            String toName = message1.getToName();
            group = toName;
            //获取发送的内容
            String msg = message1.getMessage();
            String type = message1.getType();
            //转换内容，把内容中的'替换为"


            //单独对公告进行处理
            if(type != null && type.equals("announce")){
                String username = ((User) httpSession.getAttribute("loginUser")).getName();
                //证明发布的是群公告
                String text = CommonUtil.getGroupMsg(false,((User) httpSession.getAttribute("loginUser")).getName(), msg, "announce", toName, null);
                //发送到每一个用户的客户端
                //遍历set集合发送给所有的用户
                Set<String> inGroupNames = getInGroupNames();
                //群聊
                for (String inGroupName : inGroupNames) {
                    if(!inGroupName.equals(username)){
                        //接受到的消息不要发给自己
                        synchronized (session) {
                            onlineUsers.get(inGroupName).session.getBasicRemote().sendText(text);
                        }
                    }
                }
            }


            String replace = msg.replace("'", "\"");
            String groupAccount = null;
            //存入数据库
            Groups groups = new Groups("groupName="+toName);
            List<Groups> allGroups = new UserAndGroupController().findAllGroups(groups);
            if(allGroups.size() != 0){
                groupAccount = allGroups.get(0).getGroupAccount();
            }
            GroupChat groupChat = new GroupChat(0, groupAccount, ((User)httpSession.getAttribute("loginUser")).getName(),replace, "text");
            new GroupChatController().addGroupChat(groupChat);

            //获取本session的对象名字
            String username = ((User) httpSession.getAttribute("loginUser")).getName();
            //获取session的头像路径
            String headProtrait = message1.getHead();
            //通过指定格式推送给用户
            String text = CommonUtil.getGroupMsg(false,((User) httpSession.getAttribute("loginUser")).getName(), msg, "text", toName, headProtrait);
            //遍历set集合发送给所有的用户
            Set<String> inGroupNames = getInGroupNames();
                //群聊
            for (String inGroupName : inGroupNames) {
                if(!inGroupName.equals(username)){
                    //接受到的消息不要发给自己
                    synchronized (session) {
                        onlineUsers.get(inGroupName).session.getBasicRemote().sendText(text);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void OnMessage(byte[] inputStream, Session session, boolean last){
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
                    GroupChatEndpoint groupChatEndpoint = onlineUsers.get(name);
                    //每一个chatEndpoint都对应了一个session对象
                    synchronized (this.session) {
                        groupChatEndpoint.session.getBasicRemote().sendBinary(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取在线的该群聊的所有用户
    private Set<String> getInGroupNames() throws Exception {
        //根据群聊来找出群聊在线的用户
        Set<String> groupSet = new HashSet<>();
        Set<String> onlinePerson = onlineUsers.keySet();
        //调用方法找出群聊的所有成员
        UserInGroup userInGroup = new UserInGroup("groupName="+group);
        List<UserInGroup> userInGroups = new UserAndGroupController().findUserInGroups(userInGroup);
        //获取所有的用户
        if(userInGroups.size() != 0){
            for (UserInGroup inGroup : userInGroups) {
                if(onlinePerson.contains(inGroup.getUserName())){
                    //如果查到群聊的人在已登陆状态的人中，就加入set集合
                    groupSet.add(inGroup.getUserName());
                }
            }
            return groupSet;
        }
        return null;
    }

    //获取在线的该群聊的所有用户
    private Set<String> getNames() throws Exception {
        return onlineUsers.keySet();
    }

    @OnClose
    public void onClose(){
        User loginUser = (User)httpSession.getAttribute("loginUser");
        //获取下线的用户
        String username = loginUser.getName();
        onlineUsers.remove(username);
        //关闭了，就删除了。
    }

    //推送系统消息
    private void broadcastAllUsers(String message){
        //要将该消息推送给所有的客户端，遍历
        try {
            Set<String> strings = onlineUsers.keySet();
            for (String name : strings) {
                GroupChatEndpoint groupChatEndpoint = onlineUsers.get(name);
                //每一个chatEndpoint都对应了一个session对象
                synchronized (this.session) {
                    groupChatEndpoint.session.getBasicRemote().sendText(message);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
