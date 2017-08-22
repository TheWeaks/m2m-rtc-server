package the.weaks.rtc.groupcall.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import the.weaks.rtc.groupcall.GroupCallApp;
import the.weaks.rtc.groupcall.module.Room;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.UUID;

/**
 * Created by tzh on 2017/8/21.
 *
 * @author tzh
 * @since 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GroupCallApp.class)
@SpringBootTest
@Transactional
public class RoomMapperTest {
    @Resource
    private RoomMapper roomMapper;

    @Test
    public void findByRid() throws Exception {
        Room room = roomMapper.findByRid("1");
        System.out.println(room);
    }
    @Test
    public void findByOrderNum() throws Exception {
        Room room = roomMapper.findByOrderNum("1");
        System.out.println(room);
    }
    @Test
    public void createRoom() throws Exception {
        int num  = roomMapper.createRoom(UUID.randomUUID().toString(), new Date(new java.util.Date().getTime()),1);
        System.out.println(num);
    }
}