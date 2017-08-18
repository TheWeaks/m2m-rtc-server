package the.weaks.rtc.groupcall.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import the.weaks.rtc.groupcall.GroupCallApp;


/**
 * Created by tzh on 2017/8/18.
 *
 * @author tzh
 * @since 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GroupCallApp.class)
@SpringBootTest
//@Transactional
public class UserMapperTest {
    @Test
    public void findByName() throws Exception {
        System.out.println("success");
    }

}