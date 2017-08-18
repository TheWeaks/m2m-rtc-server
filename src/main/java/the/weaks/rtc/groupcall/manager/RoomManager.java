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

package the.weaks.rtc.groupcall.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import the.weaks.rtc.groupcall.session.RoomSession;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class RoomManager {

    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Autowired
    private KurentoClient kurento;

    private final ConcurrentMap<Number, RoomSession> rooms = new ConcurrentHashMap<>();

    /**
     * Looks for a room in the active room list.
     *
     * @param roomId the name of the room
     * @return the room if it was already created, or a new one if it is the first time this room is
     * accessed
     */
    public RoomSession getRoom(Number roomId) {
        log.debug("Searching for roomSession {}", roomId);
        RoomSession roomSession = rooms.get(roomId);

        if (roomSession == null) {
            log.debug("RoomSession {} not existent. Will create now!", roomId);
            roomSession = createRoom(roomId);
        }
        log.debug("RoomSession {} found!", roomId);

        return roomSession;
    }

    /**
     * Removes a roomSession from the list of available rooms.
     *
     * @param roomSession the roomSession to be removed
     */
    public void removeRoom(RoomSession roomSession) {
        this.rooms.remove(roomSession.getRoomId());
        roomSession.close();
        log.info("RoomSession {} removed and closed", roomSession.getRoomId());
    }

    public RoomSession createRoom(Number roomId) {
        RoomSession roomSession = rooms.get(roomId);
        if (roomSession == null)
            roomSession = new RoomSession(roomId, kurento.createMediaPipeline());
        rooms.put(roomId, roomSession);
        return roomSession;
    }
}
