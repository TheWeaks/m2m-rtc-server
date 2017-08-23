package the.weaks.rtc.groupcall.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import the.weaks.rtc.groupcall.GroupCallApp;
import the.weaks.rtc.groupcall.module.User;

import java.util.UUID;


/**
 * Created by tzh on 2017/8/18.
 *
 * @author tzh
 * @since 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GroupCallApp.class)
@SpringBootTest
@Transactional
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void findByName() throws Exception {
        System.out.println("userMapperTest");
        User user = userMapper.findByName("name");
        System.out.println(user);
    }
    @Test
    public void findByUid() throws Exception {
        System.out.println("userMapperTest");
        User user = userMapper.findByUid("1");
        System.out.println(user);
    }

    @Test
    @Rollback
    public void insert() {
        System.out.println("number:"+userMapper.insertUser(((Integer)(int)(Math.random()*1000)).toString(),UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()));
    }

}