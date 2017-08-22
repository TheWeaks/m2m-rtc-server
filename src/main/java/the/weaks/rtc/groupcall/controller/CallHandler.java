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

package the.weaks.rtc.groupcall.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.kurento.client.IceCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import the.weaks.rtc.groupcall.exception.PermissionDeniedException;
import the.weaks.rtc.groupcall.exception.UserNotFoundException;
import the.weaks.rtc.groupcall.module.User;
import the.weaks.rtc.groupcall.service.RoomManager;
import the.weaks.rtc.groupcall.service.UserRegistry;
import the.weaks.rtc.groupcall.session.RoomSession;
import the.weaks.rtc.groupcall.session.UserSession;

import java.io.IOException;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @author frozonTzh (tzh1234@gmail.com)
 * @since 4.3.1
 */
public class CallHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(CallHandler.class);

    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private UserRegistry registry;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        log.info("message:{}",jsonMessage);
        try {
            final UserSession user = registry.getBySession(session);
            if (user != null) {
                log.debug("Incoming message from user '{}': {}", user.getUserId(), jsonMessage);
            } else {
                log.debug("Incoming message from new user: {}", jsonMessage);
            }
            switch (jsonMessage.get("type").getAsString()) {
                case "join":
                    joinRoom(jsonMessage, session);
                    break;
                case "receiveVideoFrom":
                    final Number senderId = jsonMessage.get("userId").getAsNumber();
                    final UserSession sender = registry.getById(senderId);
                    final String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
                    if (user == null)
                        break;
                    user.receiveVideoFrom(sender, sdpOffer);
                    break;
                case "onIceCandidate":
                    JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

                    if (user != null) {
                        IceCandidate cand = new IceCandidate(candidate.get("candidate").getAsString(),
                                candidate.get("sdpMid").getAsString(), candidate.get("sdpMLineIndex").getAsInt());
                        user.addCandidate(cand, jsonMessage.get("userId").getAsNumber());
                    }
                    break;
                case "sendMessage":
                    sendMessage(user, jsonMessage);
                    break;
                case "uploadSuccess":
                    sendFileURL(user, jsonMessage);
                    break;
                case "leave":
                    session.close();
                    break;
                default:
                    broadcast(jsonMessage, user);
                    break;
            }
        }catch (NullPointerException e)
        {
            log.warn("unrecognizedMessage");
            JsonObject error = new JsonObject();
            error.addProperty("type", "unrecognizedMessage");
            error.add("message",jsonMessage);
            session.sendMessage(new TextMessage(error.toString()));
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        UserSession user = registry.removeBySession(session);
        if (user != null)
            try {
                leaveRoom(user);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        log.info("user id {} is exit", user != null ? user.getUserId() : "null");
    }

    private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {
        final Number roomId = params.get("room").getAsNumber();
        final Number userId = params.get("userId").getAsNumber();
        log.info("PARTICIPANT {}: trying to join room {}", userId, roomId);
        try {
            User u = registry.findByUid(userId);
            roomManager.checkExist(roomId,userId);
            final UserSession user = roomManager.getRoom(roomId).join(u, session);
            registry.register(user);
        } catch (UserNotFoundException e) {
            log.warn(e.getMessage());
            JsonObject error = new JsonObject();
            error.addProperty("type", "userNotFound");
            error.addProperty("userId", e.getUserId());
            session.sendMessage(new TextMessage(error.toString()));
            session.close();
        }
        catch (PermissionDeniedException e) {
            log.warn(e.getMessage());
            JsonObject error = new JsonObject();
            error.addProperty("type", "permissionDeny");
            error.addProperty("userId", e.getUserId());
            error.addProperty("roomId", e.getRoomId());
            session.sendMessage(new TextMessage(error.toString()));
            session.close();
        }
    }

    private void leaveRoom(UserSession user) throws IOException {
        final RoomSession roomSession = roomManager.getRoom(user.getRoomId());
        roomSession.leave(user);
        if (roomSession.getParticipants().isEmpty()) {
            roomManager.pauseRoom(roomSession);
        }

    }

    private void sendMessage(UserSession user, JsonObject message) throws IOException {
        final RoomSession roomSession = roomManager.getRoom(user.getRoomId());
        final String msg = message.get("message").getAsString();
        final Number userId = user.getUserId();
        roomSession.sendMessage(userId, msg);
    }

    private void sendFileURL(UserSession user, JsonObject message) throws IOException {
        final RoomSession roomSession = roomManager.getRoom(user.getRoomId());
        final Number sender = message.get("userId").getAsNumber();
        final String fileName = message.get("fileName").getAsString();
        final String fileUrl = message.get("fileUrl").getAsString();
        roomSession.sendFileURL(sender, fileName, fileUrl);
    }

    private void broadcast(JsonObject object, UserSession user) throws IOException {
        final RoomSession roomSession = roomManager.getRoom(user.getRoomId());
        roomSession.broadcast(object);
    }
}
