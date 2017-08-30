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
    private Integer roomId;
    private String orderNum;
    private Date date;
    private Integer rState;

    public Room(Integer roomId, String orderNum, java.sql.Date date, Integer rState) {
        if (roomId == null)
            this.roomId = null;
        else
            this.roomId = roomId;
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

    public Room(String orderNum, java.sql.Date date, Integer rState) {
        this.orderNum = orderNum;
        if (date == null)
            this.date = null;
        else
            this.date = new Date(date.getTime());
        this.rState = rState;
    }

    public Number getRoomId() {
        return roomId;
    }

    public Integer getrState() {
        return rState;
    }

    public void setrState(Integer rState) {
        this.rState = rState;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
