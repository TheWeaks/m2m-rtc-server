package the.weaks.rtc.groupcall.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import the.weaks.rtc.groupcall.GroupCallApp;
import the.weaks.rtc.groupcall.exception.RoomNotExistException;
import the.weaks.rtc.groupcall.session.RoomSession;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by tzh on 2017/8/29.
 *
 * @author tzh
 * @since 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GroupCallApp.class)
@SpringBootTest
@Transactional
public class RoomManagerTest {
    @Resource
    private RoomManager roomManager;

    @Test
    @Transactional
    @Rollback
    public void createRoom() throws Exception {
        RoomSession roomSession = roomManager.createRoom(UUID.randomUUID().toString());
        System.out.println(roomSession.getRoomId());
    }

    @Test
    public void startRoom() throws Exception {
        try {
            roomManager.startRoom(234);
        } catch (RoomNotExistException e) {
            e.printStackTrace();
        }
    }
}