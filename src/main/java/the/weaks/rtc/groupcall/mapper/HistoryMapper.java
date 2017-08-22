package the.weaks.rtc.groupcall.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import the.weaks.rtc.groupcall.module.History;

/**
 * Created by tzh on 2017/8/22.
 *
 * @author tzh
 * @since 1.7
 */
@Component
@Mapper
public interface HistoryMapper {
    @Insert("INSERT INTO History VALUES (#{id},#{rid},#{uid},#{date},#{htype},#{message})")
    int logHistory(History history);
}
