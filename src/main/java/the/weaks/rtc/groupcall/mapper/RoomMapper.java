package the.weaks.rtc.groupcall.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import the.weaks.rtc.groupcall.module.Room;

import java.sql.Date;

/**
 * Created by tzh on 2017/8/21.
 *
 * @author tzh
 * @since 1.7
 */
@Mapper
@Component
public interface RoomMapper {
    @Select("SELECT * FROM Room WHERE rid=#{rid}")
    Room findByRid(@Param("rid") String rid);

    @Select("SELECT * FROM Room WHERE ordernum=#{ordernum}")
    Room findByOrderNum(@Param("ordernum") String ordernum);

    @Insert("INSERT Room(ordernum, date, rstate) VALUES (#{ordernum},#{date},#{rstate})")
    int createRoom(@Param("ordernum") String ordernum, @Param("date") Date date, @Param("rstate") Integer rstate);
}