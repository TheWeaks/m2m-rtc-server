package the.weaks.rtc.groupcall.module;

import the.weaks.rtc.groupcall.exception.PermissionDeniedException;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tzh on 2017/8/16.
 *
 * @author tzh
 * @since 1.7
 */
public class Room {
    private final Number roomId;
    private final ConcurrentLinkedQueue<User> participants = new ConcurrentLinkedQueue<>();

    public void add(User user) {
        participants.add(user);
    }

    private Room(Number roomId) {
        this.roomId = roomId;
    }

    public void checkExist(User user) throws PermissionDeniedException {
//        if(!participants.contains(user)) throw new PermissionDeniedException(user.getId(),roomId);
        if(user.getId().intValue()<0)throw new PermissionDeniedException(user.getId(),roomId);
    }
    public static Room getRoom(Number roomId){
        return new Room(roomId);
    }
}
