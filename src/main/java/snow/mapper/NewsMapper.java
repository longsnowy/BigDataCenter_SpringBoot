package snow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import snow.entity.Keywords;
import snow.entity.News;
import snow.entity.User;

import java.util.HashMap;
import java.util.List;

@org.apache.ibatis.annotations.Mapper
@Repository
public interface NewsMapper extends BaseMapper<User> {

    //查询表数据
    @Select("select * from cloud.${tablename}")
    List<HashMap<String,Object>>queryNews(@Param("tablename") String tablename);


    //查询表数据
    @Select("select count(*) from cloud.${tablename}")
    Integer getTotals(@Param("tablename") String tablename);


    @Select("select * from cloud.${tablename} limit 200")
    List<Keywords> getKeywords(@Param("tablename") String tablename);

}
