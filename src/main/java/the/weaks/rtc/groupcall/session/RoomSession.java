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

package the.weaks.rtc.groupcall.session;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.kurento.client.Continuation;
import org.kurento.client.MediaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;
import the.weaks.rtc.groupcall.mapper.FileMapper;
import the.weaks.rtc.groupcall.module.FileInfo;
import the.weaks.rtc.groupcall.module.Room;
import the.weaks.rtc.groupcall.module.User;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @author frozonTzh (tzh1234@gmai.com)
 * @since 4.3.1
 */
public class RoomSession implements Closeable {
    private final Logger log = LoggerFactory.getLogger(RoomSession.class);

    private final ConcurrentMap<Number, UserSession> participants = new ConcurrentHashMap<>();
    private final MediaPipeline pipeline;
    private final Number roomId;

    @Autowired
    private FileMapper fileMapper;

    public Number getRoomId() {
        return roomId;
    }

    public RoomSession(Room room, MediaPipeline pipeline) {
        this.roomId = room.getRoomId();
        this.pipeline = pipeline;
        log.info("ROOM {} has been created", room.getRoomId());
    }

    @PreDestroy
    private void shutdown() {
        this.close();
    }

    public UserSession join(User user, WebSocketSession session) throws IOException {
        final UserSession participant = new UserSession(user, this.roomId, session, this.pipeline);
        log.info("ROOM {}: adding participant {}", roomId, user.getId());
        joinRoom(participant);
        participants.put(participant.getUserId(), participant);
        sendParticipantNames(participant);
        return participant;
    }

    public void leave(UserSession user) throws IOException {
        if (!(null == user.getUserId() || (null == user.getUser()))) {
            log.debug("PARTICIPANT {}: Leaving room {}", user.getUserId(), this.roomId);
            this.removeParticipant(user.getUserId());
        }
        user.close();
    }

    private Collection<Number> joinRoom(UserSession newParticipant) throws IOException {
        final JsonObject newParticipantMsg = new JsonObject();
        newParticipantMsg.addProperty("type", "newParticipantArrived");
        newParticipantMsg.add("participant", newParticipant.getUser().toJson());
        final List<Number> participantsList = new ArrayList<>(participants.values().size());
        log.debug("ROOM {}: notifying other participants of new participant {}", roomId,
                newParticipant.getUserId());

        for (final UserSession participant : participants.values()) {
            try {
                participant.sendMessage(newParticipantMsg);
            } catch (final IOException e) {
                log.debug("ROOM {}: participant {} could not be notified", roomId, participant.getUserId(), e);
            }
            participantsList.add(participant.getUserId());
        }

        return participantsList;
    }

    private void removeParticipant(Number userId) throws IOException {
        participants.remove(userId);

        log.debug("ROOM {}: notifying all users that {} is leaving the room", this.roomId, userId);

        final List<Number> unnotifiedParticipants = new ArrayList<>();
        final JsonObject participantLeftJson = new JsonObject();
        participantLeftJson.addProperty("type", "participantLeft");
        participantLeftJson.addProperty("userId", userId);
        for (final UserSession participant : participants.values()) {
            try {
                participant.cancelVideoFrom(userId);
                participant.sendMessage(participantLeftJson);
            } catch (final IOException e) {
                unnotifiedParticipants.add(participant.getUserId());
            }
        }

        if (!unnotifiedParticipants.isEmpty()) {
            log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.roomId,
                    unnotifiedParticipants, userId);
        }

    }

    public void sendParticipantNames(UserSession user) throws IOException {
        final JsonArray participantsArray = new JsonArray();
        for (final UserSession participant : this.getParticipants()) {
            if (!participant.equals(user)) {
                final JsonElement participantName = participant.getUser().toJson();
                participantsArray.add(participantName);
            }
        }
        final JsonObject existingParticipantsMsg = new JsonObject();
        existingParticipantsMsg.addProperty("type", "existingParticipants");
        existingParticipantsMsg.add("participants", participantsArray);
        log.info("PARTICIPANT {}: sending a list of {} participants", user.getUserId(),
                participantsArray.size());
        user.sendMessage(existingParticipantsMsg);
    }

    public void sendMessage(Number sender, String message) throws IOException {
        JsonObject textMsg = new JsonObject();
        textMsg.addProperty("type", "receiveTextMessage");
        textMsg.addProperty("userId", sender);
        textMsg.addProperty("message", message);
        broadcast(textMsg);
    }

    public String sendFileURL(Number sender, String fileName, String fileUrl,String fileType) throws IOException {
        JsonObject fileURLMsg = new JsonObject();
        fileURLMsg.addProperty("type", "fileUploaded");
        fileURLMsg.addProperty("userId", sender);
        fileURLMsg.addProperty("fileName", fileName);
        fileURLMsg.addProperty("fileUrl", fileUrl);
        fileURLMsg.addProperty("fileType",fileType);
        String fid =UUID.randomUUID().toString();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFid(fid);
        fileInfo.setFname(fileName);
        fileInfo.setUrl(fileUrl);
        fileInfo.setUid(sender.toString());
        fileInfo.setRid(this.roomId);
        fileInfo.setFtype(fileType);
        fileMapper.newFileLog(fileInfo);
        broadcast(fileURLMsg);
        return fid;
    }

    public void broadcast(JsonObject message) throws IOException {
        final List<Number> unnotifiedParticipants = new ArrayList<>();
        for (final UserSession participant : participants.values()) {
            try {
                participant.sendMessage(message);
            } catch (final IOException e) {
                unnotifiedParticipants.add(participant.getUserId());
            }
        }

        if (!unnotifiedParticipants.isEmpty()) {
            log.debug("ROOM {}: The users {} could not be notified ", this.roomId, unnotifiedParticipants);
        }
    }

    public Collection<UserSession> getParticipants() {
        return participants.values();
    }

    public UserSession getParticipant(Number userId) {
        return participants.get(userId);
    }

    @Override
    public void close() {
        for (final UserSession user : participants.values()) {
            try {
                user.close();
            } catch (IOException e) {
                log.debug("ROOM {}: Could not invoke close on participant {}", this.roomId, user.getUserId(),
                        e);
            }
        }

        participants.clear();

        pipeline.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("ROOM {}: Released Pipeline", RoomSession.this.roomId);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release Pipeline", RoomSession.this.roomId);
            }
        });

        log.debug("RoomSession {} closed", this.roomId);
    }
}
