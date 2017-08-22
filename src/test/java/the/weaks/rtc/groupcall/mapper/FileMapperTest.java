package the.weaks.rtc.groupcall.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import the.weaks.rtc.groupcall.GroupCallApp;
import the.weaks.rtc.groupcall.module.FileInfo;

import javax.annotation.Resource;
import java.util.UUID;

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
public class FileMapperTest {
    @Resource
    private FileMapper fileMapper;

    @Test
    public void newFileLog() throws Exception {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFid(UUID.randomUUID().toString());
        fileInfo.setFname(UUID.randomUUID().toString());
        fileInfo.setUrl(UUID.randomUUID().toString());
        fileInfo.setUid("1");
        fileInfo.setRid("1");
        fileMapper.newFileLog(fileInfo);
    }
    @Test
    public void listByRid()throws Exception{
        for (FileInfo fileInfo : fileMapper.listByRid("1")) {
            System.out.println(fileInfo.getFname());
        }
    }

}