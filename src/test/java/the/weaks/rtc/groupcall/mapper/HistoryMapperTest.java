package the.weaks.rtc.groupcall.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import the.weaks.rtc.groupcall.GroupCallApp;
import the.weaks.rtc.groupcall.module.History;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by tzh on 2017/8/22.
 *
 * @author tzh
 * @since 1.7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GroupCallApp.class)
@SpringBootTest
@Transactional
public class HistoryMapperTest {
    @Autowired
    @Resource
    private HistoryMapper historyMapper;

    @Test
    public void logHistory() throws Exception {
        History history = new History();
        history.setRid(1);
        history.setUid("1");
        history.setDate(new java.sql.Date(new Date().getTime()));
        historyMapper.logHistory(history);
        System.out.println(history.getId());

    }

}