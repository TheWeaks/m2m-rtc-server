package the.weaks.rtc.groupcall.module;

import java.sql.Date;

/**
 * Created by tzh on 2017/8/22.
 *
 * @author tzh
 * @since 1.7
 */
public class History {
    private int id;
    private String rid;
    private String uid;
    private Date date;
    private int htype;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHtype() {
        return htype;
    }

    public void setHtype(int htype) {
        this.htype = htype;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
