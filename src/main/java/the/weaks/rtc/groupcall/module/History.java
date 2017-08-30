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
    private Integer rid;
    private String uid;
    private Date date;
    private int hType;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Number rid) {
        this.rid = rid.intValue();
    }

    public void setRid(Integer rid) {
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

    public int gethType() {
        return hType;
    }

    public void sethType(int hType) {
        this.hType = hType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
