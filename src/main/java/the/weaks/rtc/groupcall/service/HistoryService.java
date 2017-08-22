package the.weaks.rtc.groupcall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.weaks.rtc.groupcall.mapper.HistoryMapper;
import the.weaks.rtc.groupcall.module.History;

/**
 * Created by tzh on 2017/8/22.
 *
 * @author tzh
 * @since 1.7
 */
@Service
public class HistoryService {
    @Autowired
    private HistoryMapper historyMapper;

    public int logHistory(String uid, String rid, int type, String message) {
        History history = new History();
        history.setUid(uid);
        history.setRid(rid);
        history.setDate(new java.sql.Date(new java.util.Date().getTime()));
        history.setHtype(type);
        history.setMessage(message);
        return historyMapper.logHistory(history);


    }

    public int logHistory(String uid, String rid, int type) {
        return logHistory(uid, rid, type, null);
    }
}
