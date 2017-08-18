package the.weaks.rtc.groupcall.exception;

/**
 * Created by tzh on 2017/8/9.
 *
 * @author tzh
 * @since 1.7
 */
public class UserNotFoundException extends Exception {

    public Number getUserId() {
        return userId;
    }

    public void setUserId(Number userId) {
        this.userId = userId;
    }

    private Number userId;

    public UserNotFoundException(Number userId) {
        this.userId = userId;
    }
    @Override
    public String getMessage() {
        return "A user with id of "+userId+" does not exist";
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
