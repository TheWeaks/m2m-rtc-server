package the.weaks.rtc.groupcall.module;

import java.sql.Date;

/**
 * Created by tzh on 2017/8/21.
 *
 * @author tzh
 * @since 1.7
 */
public class RoomMember {
    private int id;
    private String rid;
    private String uid;
    private Date joinTime;
    private Integer rmState;

    public RoomMember() {
    }

    public RoomMember(String rid, String uid, Date joinTime) {
        this.rid = rid;
        this.uid = uid;
        this.joinTime = joinTime;
    }

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

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
    public Date getJointime() {
        return joinTime;
    }

    public void setJointime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Integer getRmstate() {
        return rmState;
    }

    public void setRmstate(Integer rmState) {
        this.rmState = rmState;
    }

    public Integer getRmState() {
        return rmState;
    }

    public void setRmState(Integer rmState) {
        this.rmState = rmState;
    }
}
