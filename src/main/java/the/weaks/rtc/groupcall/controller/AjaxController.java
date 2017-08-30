package the.weaks.rtc.groupcall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the.weaks.rtc.groupcall.service.RoomManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tzh on 2017/8/14.
 *
 * @author tzh
 * @since 1.7
 */
@RestController
@RequestMapping("/ajax")
public class AjaxController {
    @Autowired
    private RoomManager roomManager;

    @RequestMapping("/join")
    public Map<String, Object> join(String uid, Integer rid) {
        Map<String, Object> resp = new HashMap<>();
        try {
            roomManager.checkExist(rid, uid);
            resp.put("status", 0);
            Map<String, Object> data = new HashMap<>();
            data.put("participants", roomManager.getAllMember(rid));
            data.put("rid", rid);
            resp.put("data", data);
        } catch (Exception e) {
            resp.put("status", -1);
            resp.put("message", "非法请求");
        }
        return resp;
    }

    @RequestMapping("/createRoom")
    public String createRoom(String ordNum) {
        if (ordNum == null)
            return "需要定单编号";
        else {
            roomManager.createRoom(ordNum);
            return "房间添加成功";
        }
    }

}
