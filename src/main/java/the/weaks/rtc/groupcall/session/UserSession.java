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

import com.google.gson.JsonObject;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import the.weaks.rtc.groupcall.module.User;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @author frozonTzh (tzh1234@gmai.com)
 * @since 4.3.1
 */
public class UserSession implements Closeable {
//    private static final String FileURL = "file:///tmp/kurento/";

    private static final Logger log = LoggerFactory.getLogger(UserSession.class);

//    private final String recorderFilePath;

    private final WebSocketSession session;

    private final MediaPipeline pipeline;

//    private final RecorderEndpoint recorderEndpoint;

    private final Number roomId;

    private final WebRtcEndpoint outgoingMedia;

    private final ConcurrentMap<Number, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();

    private final User user;

    public UserSession(final User user, Number roomId, final WebSocketSession session,
                       MediaPipeline pipeline) throws IOException {
        this.pipeline = pipeline;
        this.session = session;
        this.roomId = roomId;
        this.user = user;
        this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline).build();
        this.outgoingMedia.addIceCandidateFoundListener(
                new EventListener<IceCandidateFoundEvent>() {
                    @Override
                    public void onEvent(IceCandidateFoundEvent event) {
                        JsonObject response = new JsonObject();
                        response.addProperty("type", "iceCandidate");
                        response.addProperty("userId", user.getId());
                        response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                        try {
                            synchronized (session) {
                                session.sendMessage(new TextMessage(response.toString()));
                            }
                        } catch (IOException e) {
                            log.debug(e.getMessage());
                        }
                    }
                });
//        this.recorderEndpoint = getRecoderEndPoint(this.outgoingMedia);

    }

    public WebRtcEndpoint getOutgoingWebRtcPeer() {
        return outgoingMedia;
    }

    public Number getUserId() {
        if (user == null)
            return null;
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    public WebSocketSession getSession() {
        return session;
    }

    /**
     * The room to which the user is currently attending.
     *
     * @return The room
     */
    public Number getRoomId() {
        return this.roomId;
    }

    public void receiveVideoFrom(UserSession sender, String sdpOffer) throws IOException {
        log.info("USER {}: connecting with {} in room {}", this.user.getId(), sender.getUserId(), this.roomId);

        log.trace("USER {}: SdpOffer for {} is {}", this.user.getId(), sender.getUserId(), sdpOffer);

        final String ipSdpAnswer = this.getEndpointForUser(sender).processOffer(sdpOffer);
        final JsonObject scParams = new JsonObject();
        scParams.addProperty("type", "receiveVideoAnswer");
        scParams.addProperty("userId", sender.getUserId());
        scParams.addProperty("sdpAnswer", ipSdpAnswer);

        log.trace("USER {}: SdpAnswer for {} is {}", this.user.getId(), sender.getUserId(), ipSdpAnswer);
        this.sendMessage(scParams);
        log.debug("gather candidates");
        this.getEndpointForUser(sender).gatherCandidates();
//        this.recorderEndpoint.record();
    }

    private WebRtcEndpoint getEndpointForUser(final UserSession sender) {
        if (sender.getUserId().equals(user.getId())) {
            log.debug("PARTICIPANT {}: configuring loopback", user.getId());
            return outgoingMedia;
        }

        log.debug("PARTICIPANT {}: receiving video from {}", user.getId(), sender.getUserId());

        WebRtcEndpoint incoming = incomingMedia.get(sender.getUserId());
        if (incoming == null) {
            log.debug("PARTICIPANT {}: creating new endpoint for {}", user.getId(), sender.getUserId());
            incoming = new WebRtcEndpoint.Builder(pipeline).build();

            incoming.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {
                @Override
                public void onEvent(IceCandidateFoundEvent event) {
                    JsonObject response = new JsonObject();
                    response.addProperty("type", "iceCandidate");
                    response.addProperty("userId", sender.getUserId());
                    response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                    try {
                        synchronized (session) {
                            session.sendMessage(new TextMessage(response.toString()));
                        }
                    } catch (IOException e) {
                        log.debug(e.getMessage());
                    }

                }
            });

            incomingMedia.put(sender.getUserId(), incoming);
        }

        log.debug("PARTICIPANT {}: obtained endpoint for {}", user.getId(), sender.getUserId());
        sender.getOutgoingWebRtcPeer().connect(incoming);

        return incoming;
    }

//    private RecorderEndpoint getRecoderEndPoint(WebRtcEndpoint webRtcEndpoint) {
//        RecorderEndpoint recorder = new RecorderEndpoint.Builder(pipeline, recorderFilePath)
//                .withMediaProfile(MediaProfileSpecType.MP4).build();
//
//        recorder.addRecordingListener(event -> {
//            JsonObject response = new JsonObject();
//            response.addProperty("id", "recording");
//            try {
//                synchronized (session) {
//                    session.sendMessage(new TextMessage(response.toString()));
//                }
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        });
//
//        recorder.addStoppedListener(event -> {
//            JsonObject response = new JsonObject();
//            response.addProperty("id", "stopped");
//            try {
//                synchronized (session) {
//                    session.sendMessage(new TextMessage(response.toString()));
//                }
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        });
//
//        recorder.addPausedListener(event -> {
//            JsonObject response = new JsonObject();
//            response.addProperty("id", "paused");
//            try {
//                synchronized (session) {
//                    session.sendMessage(new TextMessage(response.toString()));
//                }
//            } catch (IOException e) {
//                log.error(e.getMessage());
//            }
//        });
//        webRtcEndpoint.connect(recorder, MediaType.AUDIO);
//        webRtcEndpoint.connect(recorder, MediaType.VIDEO);
//        return recorder;
//    }

    public void cancelVideoFrom(final Number senderId) {
        log.debug("PARTICIPANT {}: canceling video reception from {}", user.getId(), senderId);
        final WebRtcEndpoint incoming = incomingMedia.remove(senderId);

        log.debug("PARTICIPANT {}: removing endpoint for {}", user.getId(), senderId);
        incoming.release(new Continuation<Void>() {
            @Override
            public void onSuccess(Void result) throws Exception {
                log.debug("PARTICIPANT {}: Released successfully incoming EP for {}",
                        UserSession.this.user.getId(), senderId);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release incoming EP for {}", UserSession.this.user.getId(),
                        senderId);
            }
        });
    }

    /**
     * (non-Javadoc)
     *
     * @throws IOException IOException
     */
    @Override
    public void close() throws IOException {
        log.debug("PARTICIPANT {}: Releasing resources", user.getId());
        for (final Number remoteParticipantName : incomingMedia.keySet()) {

            log.trace("PARTICIPANT {}: Released incoming EP for {}", user.getId(), remoteParticipantName);

            final WebRtcEndpoint ep = this.incomingMedia.get(remoteParticipantName);

            ep.release(new Continuation<Void>() {

                @Override
                public void onSuccess(Void result) throws Exception {
                    log.debug("PARTICIPANT {}: Released successfully incoming EP for {}",
                            UserSession.this.user.getId(), remoteParticipantName);
                }

                @Override
                public void onError(Throwable cause) throws Exception {
                    log.warn("PARTICIPANT {}: Could not release incoming EP for {}", UserSession.this.user.getId(),
                            remoteParticipantName);
                }
            });
        }

        outgoingMedia.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.debug("PARTICIPANT {}: Released outgoing EP", UserSession.this.user.getId());
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.debug("USER {}: Could not release outgoing EP", UserSession.this.user.getId());
            }
        });
    }

    public void sendMessage(JsonObject message) throws IOException {
        log.debug("USER {}: Sending message {}", user.getId(), message);
        synchronized (session) {
            session.sendMessage(new TextMessage(message.toString()));
        }
    }

    public void addCandidate(IceCandidate candidate, Number userId) {
        if (Objects.equals(user.getId(), userId)) {
            outgoingMedia.addIceCandidate(candidate);
        } else {
            WebRtcEndpoint webRtc = incomingMedia.get(userId);
            if (webRtc != null) {
                webRtc.addIceCandidate(candidate);
            }
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof UserSession)) {
            return false;
        }
        UserSession other = (UserSession) obj;
        boolean eq = user.getId().equals(other.user.getId());
        eq &= roomId.equals(other.roomId);
        return eq;
    }

    /**
     * (non-Javadoc)
     *
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + user.hashCode();
        result = 31 * result + roomId.hashCode();
        return result;
    }

//    private String now() {
//        return new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date());
//    }
}
