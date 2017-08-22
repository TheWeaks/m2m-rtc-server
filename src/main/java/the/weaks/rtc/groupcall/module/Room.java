package the.weaks.rtc.groupcall.module;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tzh on 2017/8/16.
 *
 * @author tzh
 * @since 1.7
 */
public class Room {
    private final Integer roomId;
    private final String orderNum;
    private Date date;
    private Number rState;

    public Room(String roomId, String orderNum, java.sql.Date date, Integer rState) {
        this.roomId = Integer.valueOf(roomId);
        this.orderNum = orderNum;
        if (date == null)
            this.date = null;
        else
            this.date = new Date(date.getTime());
        this.rState = rState;
    }

    public Room(Integer roomId) {
        this.roomId = roomId;
        this.orderNum = UUID.randomUUID().toString();
        this.date = new java.sql.Date(new Date().getTime());

    }

    public Number getRoomId() {
        return roomId;
    }

}
