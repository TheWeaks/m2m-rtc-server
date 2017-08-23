package the.weaks.rtc.groupcall.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import the.weaks.rtc.groupcall.module.RoomMember;

/**
 * Created by tzh on 2017/8/21.
 *
 * @author tzh
 * @since 1.7
 */
@Mapper
@Component
public interface RoomMemberMapper {
    @Select("SELECT count(*)  FROM RoomMember WHERE rid=#{rid} AND uid=#{uid}")
    Long count(@Param("rid") String rid, @Param("uid") String uid);

    @Select("SELECT rmstate  FROM RoomMember WHERE rid=#{rid} AND uid=#{uid}")
    Integer getState(@Param("rid") String rid, @Param("uid") String uid);

    @Insert("INSERT RoomMember(rid, uid, jointime, rmstate) VALUES (#{rid}, #{uid}, #{jointime}, #{rmstate})")
    int join(RoomMember roomMember);

    @Select("SELECT * from RoomMember WHERE rid=#{rid}")
    RoomMember[] listAll(@Param("rid")String rid);
}
