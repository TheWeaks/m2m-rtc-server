package the.weaks.rtc.groupcall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tzh on 2017/8/14.
 *
 * @author tzh
 * @since 1.7
 */
@RestController
@RequestMapping("/ajax")
public class AjaxController {
    @RequestMapping("/")
    public String hello() {
        return "hello world";
    }
}
