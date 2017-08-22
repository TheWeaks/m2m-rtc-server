package the.weaks.rtc.groupcall.configuration;

import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import the.weaks.rtc.groupcall.controller.CallHandler;
import the.weaks.rtc.groupcall.service.RoomManager;
import the.weaks.rtc.groupcall.service.UserRegistry;

/**
 * Created by tzh on 2017/8/18.
 *
 * @author tzh
 * @since 1.7
 */
@EnableWebSocket
@Configuration
public class KurentoConfiguration implements WebSocketConfigurer {
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
//        return KurentoClient.create();
//        return KurentoClient.create("ws://192.168.22.145:8888/kurento");
        return KurentoClient.create("ws://123.206.29.201:6666/kurento");
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(groupCallHandler(), "/groupcall")
                .setAllowedOrigins("*");
    }
}
