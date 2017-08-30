package the.weaks.rtc.groupcall.exception;

/**
 * Created by tzh on 2017/8/29.
 *
 * @author tzh
 * @since 1.7
 */
public class RoomNotExistException extends Exception {
    private Number roomId;

    public RoomNotExistException(Number roomId) {
        this.roomId = roomId;
    }

    @Override
    public String getMessage() {
        return "A room with id of " + roomId + " does not exist";
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    public Number getRoomId() {
        return roomId;
    }

    public void setRoomId(Number roomId) {
        this.roomId = roomId;
    }
}
