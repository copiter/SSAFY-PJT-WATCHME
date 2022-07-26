package com.A108.Watchme;

import com.A108.Watchme.VO.WebRTC.RoomManager;
import com.A108.Watchme.VO.WebRTC.UserRegistry;
import com.A108.Watchme.WebRTC.CallHandler;
import org.kurento.client.KurentoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


@SpringBootApplication
@EnableWebSocket
public class GroupCallApp implements WebSocketConfigurer {


    @Bean
    public UserRegistry registry() {
        return new UserRegistry();
    }

    @Bean
    public RoomManager roomManager() {
        return new RoomManager();
    }

    @Bean
    public CallHandler groupCallHandler() {
        return new CallHandler();
    }

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GroupCallApp.class, args);
    }
    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean(){
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        return container;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(groupCallHandler(), "/groupcall");
    }
}