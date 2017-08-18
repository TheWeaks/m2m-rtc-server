/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package the.weaks.rtc.groupcall;

import org.kurento.client.KurentoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import the.weaks.rtc.groupcall.controller.CallHandler;
import the.weaks.rtc.groupcall.manager.RoomManager;
import the.weaks.rtc.groupcall.manager.UserRegistry;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
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
         return KurentoClient.create("ws://192.168.22.145:8888/kurento");
//        return KurentoClient.create("ws://123.206.29.201:6666/kurento");
//        return KurentoClient.create();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GroupCallApp.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(groupCallHandler(), "/groupcall")
                .setAllowedOrigins("*");
    }
}