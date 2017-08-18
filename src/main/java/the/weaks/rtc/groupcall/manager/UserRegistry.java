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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;
import the.weaks.rtc.groupcall.session.UserSession;

/**
 * Map of users registered in the system. This class has a concurrent hash map to store users, using
 * its name as key in the map.
 *
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @author Micael Gallego (micael.gallego@gmail.com)
 * @author Ivan Gracia (izanmail@gmail.com)
 * @author frozonTzh (tzh1234@gmail.com)
 * @since 4.3.1
 */
public class UserRegistry {

    private final ConcurrentHashMap<Number, UserSession> usersById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, UserSession> usersBySessionId = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(UserRegistry.class);

    public void register(UserSession user) {
        log.debug("creating user session for" + user.getUserId());
        usersById.put(user.getUserId(), user);
        usersBySessionId.put(user.getSession().getId(), user);
    }

    public UserSession getById(Number userId) {
        return usersById.get(userId);
    }

    public UserSession getBySession(WebSocketSession session) {
        return usersBySessionId.get(session.getId());
    }

    public boolean exists(Number userId) {
        return usersById.get(userId)!=null;
    }

    public boolean exists(UserSession user) {
        return user != null && usersById.values().contains(user);
    }

    public UserSession removeBySession(WebSocketSession session) {
        final UserSession user = getBySession(session);
        log.info("user {} is exit",user);
        usersBySessionId.remove(session.getId());
        if(user!=null)
            usersById.remove(user.getUserId());
        return user;
    }
}
