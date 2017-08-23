package the.weaks.rtc.groupcall.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import the.weaks.rtc.groupcall.GroupCallApp;
import the.weaks.rtc.groupcall.module.RoomMember;

import javax.annotation.Resource;
import java.sql.Date;

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
public class RoomMemberMapperTest {
    @Resource
    private RoomMemberMapper roomMemberMapper;

    @Test
    public void count() {
        System.out.println(roomMemberMapper.count("1", "1"));
    }

    @Test
    public void getRM() {
        System.out.println(roomMemberMapper.getState("1", "1"));
    }

    @Test
    @Rollback
    public void create() {
        RoomMember roomMember = new RoomMember("1", "1",
                new Date(new java.util.Date().getTime()));
        System.out.println(roomMemberMapper.join(roomMember));
        System.out.println(roomMember.getId());
    }
    @Test
    public void listAll(){
        for (RoomMember roomMember : roomMemberMapper.listAll("1")) {
            System.out.println(roomMember.getUid());
        }
    }

}