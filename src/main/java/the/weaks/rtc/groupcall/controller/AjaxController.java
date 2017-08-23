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

    @RequestMapping("/")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/join")
    public Map<String, Object> check(String rid) {
        Map<String, Object> resp = new HashMap<>();
        if (rid == null) {
            resp.put("status", -1);
            resp.put("message", "rid is null");
        } else {
            resp.put("status", 0);
            resp.put("data", roomManager.getAllMember(rid));
        }
        return resp;
    }
}
