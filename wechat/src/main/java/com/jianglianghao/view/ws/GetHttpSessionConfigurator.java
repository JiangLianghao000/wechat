package com.jianglianghao.view.ws;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author JLHWASX   Email:2429890953@qq.com
 * @Description
 * @verdion
 * @date 2021/5/179:11
 */

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        //获取到session对象
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        //将httpsession对象存储到配置EndpointConfig中，方便获取,ServerEndpointConfig和EndpointConfig是子父类的关系
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
