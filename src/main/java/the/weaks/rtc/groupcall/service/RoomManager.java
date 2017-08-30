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

package the.weaks.rtc.groupcall.service;

import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import the.weaks.rtc.groupcall.exception.PermissionDeniedException;
import the.weaks.rtc.groupcall.exception.RoomNotExistException;
import the.weaks.rtc.groupcall.mapper.RoomMapper;
import the.weaks.rtc.groupcall.mapper.RoomMemberMapper;
import the.weaks.rtc.groupcall.mapper.UserMapper;
import the.weaks.rtc.groupcall.module.Room;
import the.weaks.rtc.groupcall.module.RoomMember;
import the.weaks.rtc.groupcall.module.User;
import the.weaks.rtc.groupcall.session.RoomSession;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class RoomManager {

    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Autowired
    private KurentoClient kurento;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomMemberMapper roomMemberMapper;

    @Autowired
    private UserMapper userMapper;

    private final ConcurrentMap<Number, RoomSession> rooms = new ConcurrentHashMap<>();

    /**
     * Looks for a room in the active room list.
     *
     * @param roomId the name of the room
     * @return the room if it was already created, or a new one if it is the first time this room is
     * accessed
     */
    public RoomSession getRoom(Number roomId) throws RoomNotExistException {
        log.debug("Searching for roomSession {}", roomId);
        RoomSession roomSession = rooms.get(roomId);

        if (roomSession == null) {
            log.debug("RoomSession {} not existent. Will create now!", roomId);
            roomSession = startRoom(roomId);
        }
        log.debug("RoomSession {} found!", roomId);

        return roomSession;
    }

    /**
     * Removes a roomSession from the list of available rooms.
     *
     * @param roomSession the roomSession to be removed
     */
    public void pauseRoom(RoomSession roomSession) {
        this.rooms.remove(roomSession.getRoomId());
        roomSession.close();
        log.info("RoomSession {} removed and closed", roomSession.getRoomId());
    }

    @Transactional
    public RoomSession createRoom(String ordNum) {
        Room room = new Room(ordNum, new Date(new java.util.Date().getTime()), 0);
        roomMapper.createRoom(room);
        RoomSession roomSession = new RoomSession(room, kurento.createMediaPipeline());
        rooms.put(roomSession.getRoomId().intValue(), roomSession);
        return roomSession;
    }

    public RoomSession startRoom(Number roomId) throws RoomNotExistException {
        try {
            RoomSession roomSession = rooms.get(roomId);
            Room room = null;
            if (roomSession == null) {
                room = this.findByRid(roomId);
            }
            roomSession = new RoomSession(room, kurento.createMediaPipeline());
            rooms.put(roomId.intValue(), roomSession);
            return roomSession;
        } catch (NullPointerException e) {
            log.warn("room {} isn\'t exist", roomId);
            throw new RoomNotExistException(roomId);
        }
    }

    public int checkRoomStatus(Number roomId) {
        RoomSession roomSession = rooms.get(roomId);
        Room room;
        if (roomSession == null) {
            room = this.findByRid(roomId);
            if (room == null)
                return -1;
            else
                return room.getrState();
        } else return 0;
    }

    @Transactional
    Room findByRid(Number rid) {
        return roomMapper.findByRid(rid.toString());
    }

    public void checkExist(Number rid, Number uid) throws PermissionDeniedException {
        if (rid == null || uid == null || roomMemberMapper.count(rid.intValue(), uid.toString()) == 0) {
            throw new PermissionDeniedException(uid, rid);
        }
    }

    public void checkExist(Integer rid, String uid) throws PermissionDeniedException {
        if ((rid == null || uid == null || roomMemberMapper.count(rid, uid) == 0)) {
            checkExist(rid, Integer.valueOf(uid));
        }
    }

    @Transactional
    public int put(Number rid, String uid) {
        RoomMember roomMember = new RoomMember(rid, uid,
                new Date(new java.util.Date().getTime()));
        return roomMemberMapper.join(roomMember);
    }

    @Transactional
    public List<User> getAllMember(Number rid) {
        List<User> users = new ArrayList<>();
        for (RoomMember roomMember : roomMemberMapper.listAll(rid.intValue())) {
            users.add(userMapper.findByUid(roomMember.getUid()));
        }
        return users;
    }
}
