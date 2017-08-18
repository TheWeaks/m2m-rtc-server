package the.weaks.rtc.groupcall.exception;

/**
 * Created by tzh on 2017/8/15.
 *
 * @author tzh
 * @since 1.7
 */
public class PermissionDeniedException extends Exception{
    public Number getUserId() {
        return userId;
    }

    public void setUserId(Number userId) {
        this.userId = userId;
    }

    private Number userId;

    public Number getRoomId() {
        return roomId;
    }

    public void setRoomId(Number roomId) {
        this.roomId = roomId;
    }

    private Number roomId;

    public PermissionDeniedException(Number userId,Number roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    @Override
    public String getMessage() {
        return "A user with id of "+userId+" did not allowed to join a room with id of "+roomId;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
