package the.weaks.rtc.groupcall.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import the.weaks.rtc.groupcall.module.FileInfo;

/**
 * Created by tzh on 2017/8/22.
 *
 * @author tzh
 * @since 1.7
 */
@Mapper
@Component
public interface FileMapper {
    @Insert("Insert File VALUES (#{fid},#{uid},#{rid},#{ftype},#{fname},#{url})")
    int newFileLog(FileInfo fileInfo);
    @Select("SELECT * FROM File WHERE rid=#{rid}")
    FileInfo[] listByRid(@Param("rid")String rid);

}

