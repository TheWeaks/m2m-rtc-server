package the.weaks.rtc.groupcall.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import the.weaks.rtc.groupcall.module.User;

/**
 * Created by tzh on 2017/8/18.
 *
 * @author tzh
 * @since 1.7
 */
@Mapper
@Component
public interface UserMapper {
    @Select("SELECT * FROM WeaksCall.User WHERE NAME = #{name}")
    User findByName(@Param("name") String name);
    @Select("SELECT * FROM WeaksCall.User WHERE uid = #{uid}")
    User findByUid(@Param("uid") String name);
    @Insert("INSERT  WeaksCall.User (uid,name,prefix,suffix) " +
            "VALUES (#{uid},#{name},#{prefix},#{suffix})")
    int insertUser(@Param("uid")String uid,@Param("name")String name,
                   @Param("prefix")String prefix,@Param("suffix")String suffix);
}
